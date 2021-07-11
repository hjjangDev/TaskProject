package com.apmall.service;

import com.apmall.dao.ApmDAO;
import com.apmall.dto.CategoryDto;
import com.apmall.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CacheService {

    @Autowired
    @Qualifier("apmDAO")
    private ApmDAO apmDAO;

    List<CategoryDto> categoryList = new ArrayList<>();
    List<ProductDto> productList = new ArrayList<>();

    private final long cacheDuration = 30 * 1000L; //캐시 유지 시간 60초

    private long categoryLoadTime;
    private long productLoadTime;

    private int prdMaxSize = 1000; //상품갯수가 1000개가 넘어가면 remove 후 add

    public List<CategoryDto> getCategoryList() throws Exception {
        long now = System.currentTimeMillis();

        if (categoryList.isEmpty() || now - categoryLoadTime > cacheDuration) {
            synchronized (categoryList) {
                categoryList.clear();
                categoryList = apmDAO.selectCategories();
                categoryLoadTime = now;
            }
        }

        return categoryList;
    }

    public List<ProductDto> getAllProductList(ProductDto productDto) throws Exception {
        long now = System.currentTimeMillis();

        if (productList.isEmpty() || now - productLoadTime > cacheDuration) {
            synchronized (categoryList) {
                productList.clear();
                cacheProduct(apmDAO.selectProducts(productDto), productDto);
                productLoadTime = now;
            }
        }

        if (productDto != null) {
            if(productDto.getCategory_name()!=null){
                return getProductListByCategoryName(productDto);
            }else if(productDto.getCategory_name()!=null){
                return getProductListByProductName(productDto);
            }
        }
        return productList;
    }

    public List<ProductDto> getProductListByCategoryName(ProductDto productDto) throws Exception {

        List<ProductDto> findList = productList.stream()
                .filter(item -> item.getCategory_name().equals(productDto.getCategory_name()))
                .collect(Collectors.toList());

        return cacheProduct(findList, productDto);
    }

    public List<ProductDto> getProductListByProductName(ProductDto productDto) throws Exception {
        List<ProductDto> findList = productList.stream()
                .filter(item -> item.getProduct_name().equals(productDto.getProduct_name()))
                .collect(Collectors.toList());

        return cacheProduct(findList, productDto);
    }

    /*
     * productDto : 사용자가 검색한 조건이 들어가있는 dto
     * prdList : 사용자가 검색한 조건에 해당하는 상품 목록
     *
     * prdList.size()가 0인 경우 cache miss : 해당 조건을 쿼리에서 한번 더 조회한다.
     * 중복이 발생할수있으니 중복 가능성이 있는 항목들을 지운다.
     *
     * LRU 사용 : 객체 내에 사용되지 않고 가장 오래 머물러 있던 항목 교체
     * 새로 추가할 prdList의 size와 현재 전체 상품 목록인 productList의 size를 더한 값이 prdMaxSize보다 큰 경우에는 list의 마지막 값을 remove한다.
     * 최근 검색한 prdList를 0번째 순서로 add한다.
     * */
    public List<ProductDto> cacheProduct(List<ProductDto> prdList, ProductDto productDto) throws Exception {

        if (prdList.size() == 0) {
            prdList = apmDAO.selectProducts(productDto);
        }

        productList.removeAll(prdList);

        while (prdList.size() + productList.size() < prdMaxSize) {
            productList.remove(productList.size() - 1);
        }

        productList.addAll(0, prdList);

        return prdList;
    }

}

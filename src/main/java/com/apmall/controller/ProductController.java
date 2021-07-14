package com.apmall.controller;

import com.apmall.dto.defaultRes.DefaultRes;
import com.apmall.dto.product.ProductDto;
import com.apmall.service.cache.CacheService;
import com.apmall.service.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;
    private final CacheService cacheService;

    public ProductController(ProductService productService, CacheService cacheService) {
        this.productService = productService;
        this.cacheService = cacheService;
    }


    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity allProductListApi(@RequestParam HashMap<String, String> paramMap) throws Exception {
        HashMap<String, Object> resultMap;
        List<ProductDto> cacheList = null;
        String key = paramMap.get("category_name") != null ? paramMap.get("category_name") : paramMap.get("product_name");

        if (key != null) {
            boolean reloadFlag = false;
            cacheList = (List<ProductDto>) cacheService.getCache(key);

            if (cacheList != null && cacheList.size() != 0) {
                if (cacheService.checkCacheExpired(key)) { //Cache 만료
                    /*
                     * case 3. Cache Data Eviction Policy
                     * 상품 목록의 경우에는 카테고리 목록보다 정보가 변동 될 가능성이 크다.
                     * 그래서 setCache 할때 추가했던 loadTime으로 만료가 됐는지 확인한다
                     * 캐시가 만료됐을 경우 Evict 한다.
                     * */
                    cacheService.evictCache(key); //Cache Evict
                    reloadFlag = true;
                }
            } else if (cacheList == null || cacheList.size() == 0) { //Cache Miss
                reloadFlag = true;
            }

            if (reloadFlag) {
                /*
                 * case 2. Data Loading and Reloading
                 * Cache Miss 발생 또는 만료됐을 경우
                 * 데이터를 reload하여 setCache 한다.
                 * */
                List<ProductDto> selectList = productService.selectProducts(paramMap);
                cacheService.setCache(selectList, key);
                cacheList = selectList != null ? (List<ProductDto>) cacheService.getCache(key) : null;
            }

            resultMap = DefaultRes.selectRes(cacheList);

        } else { //parameter가 올바르지 않을때
            resultMap = DefaultRes.badRequest();
        }


        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));
    }


    /*
     * case 5.
     * ADD / DELETE / UPDATE의 경우 처리 후 Evict 처리를 하고 데이터를 Reload 해서 바뀐 정보를 유지해야하지만
     * '현실에서는 별도의 API를 통하여 일어나는 행위'로 인해 따로 처리하지 않음
     * */

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity insertProductApi(@RequestBody ProductDto productDto) throws Exception {
        HashMap<String, Object> resultMap;
        boolean paramFlag = StringUtils.isEmpty(productDto.getProduct_name()) || StringUtils.isEmpty(productDto.getBrand_name())
                || StringUtils.isEmpty(productDto.getProduct_price()) || StringUtils.isEmpty(productDto.getCategory_no());

        if (!paramFlag) {
            int successRow = productService.insertProduct(productDto);
            resultMap = DefaultRes.updateRes(successRow);
        } else {
            resultMap = DefaultRes.badRequest();
        }
        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));
    }

    @RequestMapping(value = "/product", method = RequestMethod.PUT)
    public ResponseEntity updateProductApi(@RequestBody ProductDto productDto) throws Exception {
        HashMap<String, Object> resultMap;
        boolean paramFlag = StringUtils.isEmpty(productDto.getProduct_no()) ? true : StringUtils.isEmpty(productDto.getProduct_name()) && StringUtils.isEmpty(productDto.getProduct_price());

        if (!paramFlag) {
            int successRow = productService.updateProduct(productDto);
            resultMap = DefaultRes.updateRes(successRow);
        } else {
            resultMap = DefaultRes.badRequest();
        }

        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));
    }

    @RequestMapping(value = "/product", method = RequestMethod.DELETE)
    public ResponseEntity deleteProductApi(@RequestBody ProductDto productDto) throws Exception {
        HashMap<String, Object> resultMap;
        boolean paramFlag = StringUtils.isEmpty(productDto.getProduct_no());

        if (!paramFlag) {
            int successRow = productService.deleteProduct(productDto);
            resultMap = DefaultRes.updateRes(successRow);
        } else {
            resultMap = DefaultRes.badRequest();
        }

        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));
    }

}

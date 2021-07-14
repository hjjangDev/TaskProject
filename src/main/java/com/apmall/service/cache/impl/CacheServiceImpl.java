package com.apmall.service.cache.impl;

import com.apmall.dto.category.CategoryDto;
import com.apmall.service.cache.CacheService;
import com.apmall.service.category.CategoryService;
import com.apmall.service.product.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CacheServiceImpl implements CacheService, ApplicationRunner {

    private final CategoryService categoryService;
    private final ProductService productService;

    @Value("${cache.maxSize}")
    private int cacheMaxSize;

    @Value("${cache.product.duration}")
    private int productCacheDuration;

    Map<String, Object> cacheMap = Collections.synchronizedMap(new LinkedHashMap<>()); //순서를 유지하는 LinkedHashMap으로 캐시를 관리

    public CacheServiceImpl(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /*
    * case 2. Data Loading and Reloading
    * 프로젝트 실행시 처음 한번 실행하여 전체 카테고리 목록, 카테고리별 상품 목록 캐싱
    *
    * case 4. Cache Optimization (Cache Miss의 처리 및 최소화 방법)
    * 캐시 접근 빈도 : 카테고리 > 카테고리별 상품 > 특정 상품
    * 카테고리와 카테고리별 상품을 미리 캐싱해오게 되므로 Cache Miss의 빈도를 줄일 수 있다.
    * 특정 상품을 모두 캐싱하기엔 상품의 갯수가 많기 때문에 특정 상품은 사용자가 검색할때마다 캐싱한다.
    * */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        //전체 카테고리 목록 캐싱
        List<CategoryDto> categoryList = categoryService.selectCategories();
        setCache(categoryList, "category");

        //카테고리별 상품 목록 캐싱
        for (CategoryDto category : categoryList) {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("category_name", category.getCategory_name());

            setCache(productService.selectProducts(paramMap), category.getCategory_name());
        }
    }

    @Override
    public void setCache(Object list, String key) throws Exception {

        List<Object> beforeCacheList = (List<Object>) list;

        if (beforeCacheList == null || beforeCacheList.size() == 0 || key == null) return;

        long now = System.currentTimeMillis();

        //Cache Evict(캐시 메모리가 모두 찼을때 캐시 객체의 마지막 항목 remove)
        while (cacheMap.size() >= cacheMaxSize) {
            evictCache(null);
        }
        //카테고리가 아닌 상품 검색인 경우에는 캐시 유지 시간을 관리하기 위해 list의 0번째에 loadTime을 add
        if (!"category".equals(key)) {
            HashMap<String, String> loadTimeMap = new HashMap<>();
            loadTimeMap.put("loadTime", Long.toString(now));

            beforeCacheList.add(0, loadTimeMap);
        }

        replaceCache(beforeCacheList, key);

    }

    @Override
    public Object getCache(String key) {

        if (key == null) return null;

        List<HashMap<String, String>> cacheList = (List<HashMap<String, String>>) cacheMap.get(key);

        if (cacheList != null && cacheList.size() != 0) {
            replaceCache(cacheList, key);
            if (!"category".equals(key)) {
                cacheList.remove(0);
            }
        }
        return cacheList;
    }

    @Override
    public void evictCache(String key) {
        /*
        * case 3. Cache Data Eviction Policy
        * 캐시 메모리가 차서 Evict 처리를 해야할때 LRU 알고리즘 사용
        * 가장 최근에 사용된 적이 없는 캐시의 메모리부터 대체하기 때문에 Cache miss가 제일 적게 발생된다.
        * */
        if (key == null) {
            for (Map.Entry<String, Object> entry : cacheMap.entrySet()) {
                key = entry.getKey();
            }
        }
        cacheMap.remove(key);
    }

    //최근에 조회 한 결과를 캐시 객체의 가장 앞쪽으로 교체
    @Override
    public void replaceCache(Object beforCacheListWithTime, String key) {
        HashMap<String, Object> cloneMap = new LinkedHashMap<>();
        cacheMap.remove(key); //같은 key가 있을 수 있기 때문에 remove후 put 한다.
        cloneMap.putAll(cacheMap);

        cacheMap.clear();
        cacheMap.put(key, beforCacheListWithTime);
        cacheMap.putAll(cloneMap);
    }

    //캐시 만료 확인
    @Override
    public boolean checkCacheExpired(String key) {

        List<HashMap<String, String>> cacheList = (List<HashMap<String, String>>) cacheMap.get(key);

        long now = System.currentTimeMillis();
        long loadTime = Long.parseLong(cacheList.get(0).get("loadTime"));

        if (cacheList == null || now - loadTime > productCacheDuration) { //만료됐을때 return true
            return true;
        }

        return false;
    }
}

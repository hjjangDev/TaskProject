package com.apmall.service.cache.impl;

import com.apmall.dto.product.ProductDto;
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

    @Value("${myCache.duration}")
    private long cacheDuration;

    @Value("${myCache.maxSize}")
    private int cacheMaxSize;

    Map<String, Object> cacheMap = new LinkedHashMap<>(); //순서를 유지하는 LinkedHashMap으로 캐시를 관리
    Map<String, String> loadTimeMap;

    public CacheServiceImpl(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception { //프로젝트 실행시 처음 한번 실행하여 전체 카테고리 목록, 전체 상품 목록 캐싱
        getCacheItemList(null, "category");
        getCacheItemList(null, "product");
    }

    @Override
    public Object getCacheItemList(Map<String, String> paramMap, String flag) throws Exception {

        List<ProductDto> resultList = new ArrayList<>();
        long now = System.currentTimeMillis();

        String key_value = searchKeyValue(paramMap, flag);
        List<Map<String, String>> list = (List<Map<String, String>>) cacheMap.get(key_value);

        long loadTime = list != null ? Long.parseLong(list.get(0).get("loadTime")) : now;

        //캐시유지시간을 지났을때에도 캐시를 갱신하여 최신 상태로 유지한다.
        if (list == null || now - loadTime > cacheDuration) {
            loadTimeMap = new HashMap<>();
            loadTimeMap.put("loadTime", Long.toString(now));
            cacheItem(paramMap, flag);
        } else {
            replaceCache(list, key_value);
        }

        if (cacheMap.get(key_value) != null) {
            resultList.addAll((List<ProductDto>) cacheMap.get(key_value));
            resultList.remove(0); //return 할 list는 0번째인 loadTime 제거
        }

        return resultList;
    }

    /*
     * 캐시 객체에 들어 갈 항목을 조회하고, 캐시 객체가 꽉 찼을때 객체에 가장 오래 머물러 있던 항목을 지운다.
     *
     * 메소드가 실행되는 상황 :
     *  1. 사용자가 조회하려는 조건이 캐시에 없을 때
     *  2. 검색한 조건에 대한 캐시를 갖고있지만 해당 객체가 캐시 유지 시간을 초과했을때
     *
     * LRU 알고리즘 사용 이유 : 가장 최근에 사용된 적이 없는 캐시의 메모리부터 대체하기 때문에 Cache miss가 제일 적게 발생된다.
     *
     * */
    @Override
    public void cacheItem(Map<String, String> paramMap, String flag) throws Exception {

        Object flagObj = flag.equals("category") ? categoryService.selectCategories() : productService.selectProducts(paramMap);

        if (((List) flagObj).size() == 0) {
            return;
        }

        String key_value = searchKeyValue(paramMap, flag);
        List<Map<String, String>> flagList = (List<Map<String, String>>) flagObj;

        // Cache Eviction(캐시크기가 꽉 찼을때 캐시 객체의 마지막 항목 remove)
        while (cacheMap.size() >= cacheMaxSize) {
            String lastkey = "";
            for (Map.Entry<String, Object> entry : cacheMap.entrySet()) {
                lastkey = entry.getKey();
            }
            cacheMap.remove(lastkey);
        }

        flagList.add(0, loadTimeMap); //cache가 생성된 시간을 list의 0번째에 add (캐시에 저장된 모든 list의 0번째는 "loadTime"을 넣어 캐시 유지 시간을 관리한다.)

        replaceCache(flagList, key_value);
    }

    //최근에 조회 한 결과를 캐시 객체의 가장 앞쪽으로 배치한다.
    @Override
    public void replaceCache(Object list, String key_value) {

        Map<String, Object> cloneMap = new LinkedHashMap<>();

        cacheMap.remove(key_value); //같은 key가 있을 수 있기 때문에 remove후 put 한다.
        cloneMap.putAll(cacheMap);

        cacheMap.clear();
        cacheMap.put(key_value, list);
        cacheMap.putAll(cloneMap);

    }

    //사용자가 검색한 검색 조건을 통해 캐시에 저장 할 key값을 생성
    @Override
    public String searchKeyValue(Map<String, String> paramMap, String flag) {

        String key = flag;
        String value = "all";

        if (paramMap != null && paramMap.size() != 0) {
            Map.Entry<String, String> entry = paramMap.entrySet().iterator().next();
            key = entry.getKey();
            value = entry.getValue();
        }

        return key + "_" + value;
    }


}

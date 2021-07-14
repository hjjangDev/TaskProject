package com.apmall.scheduler;

import com.apmall.service.cache.CacheService;
import com.apmall.service.category.CategoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {

    private final long CATEGORY_FIXED_DELAY = 1000 * 60 * 60 * 12; //12h
    private final CacheService cacheService;
    private final CategoryService categoryService;

    public CacheScheduler(CacheService cacheService, CategoryService categoryService) {
        this.cacheService = cacheService;
        this.categoryService = categoryService;
    }

    /*
    * case 3. Cache Data Eviction Policy
    * 카테고리의 경우에는 상품정보보다 데이터가 변동될 확률이 적다.
    * 그래서 조회마다 Cache 만료를 체크하는것보다 Scheduler를 통해 주기적인 시간마다 카테고리를 Evict후 setCache한다.
    * */
    @Scheduled(fixedDelay = CATEGORY_FIXED_DELAY)
    public void categoryEvictReload() throws Exception {
        cacheService.evictCache("category");
        cacheService.setCache(categoryService.selectCategories(), "category");
    }
}

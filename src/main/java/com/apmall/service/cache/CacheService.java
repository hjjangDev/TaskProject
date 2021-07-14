package com.apmall.service.cache;

public interface CacheService {

    public void setCache(Object beforeCacheList, String key) throws Exception;

    public Object getCache(String key);

    public void evictCache(String key);

    public void replaceCache(Object beforCacheListWithTime, String key);

    public boolean checkCacheExpired(String key);
}

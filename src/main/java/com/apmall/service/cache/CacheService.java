package com.apmall.service.cache;

import java.util.Map;

public interface CacheService {

    public Object getCacheItemList(Map<String, String> paramMap, String flag) throws Exception;

    public void cacheItem(Map<String, String> paramMap, String flag) throws Exception;

    public void replaceCache(Object list, String key_value);

    public String searchKeyValue(Map<String, String> paramMap, String flag);

}

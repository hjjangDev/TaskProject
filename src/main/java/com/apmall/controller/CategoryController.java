package com.apmall.controller;

import com.apmall.dto.category.CategoryDto;
import com.apmall.dto.defaultRes.DefaultRes;
import com.apmall.dto.product.ProductDto;
import com.apmall.service.cache.CacheService;
import com.apmall.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CacheService cacheService;

    public CategoryController(CategoryService categoryService, CacheService cacheService) {
        this.categoryService = categoryService;
        this.cacheService = cacheService;
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity categoryListApi() throws Exception {
        HashMap<String, Object> resultMap;

        String key = "category";
        List<ProductDto> cacheList = (List<ProductDto>) cacheService.getCache(key);

        if (cacheList == null || cacheList.size() == 0) {
            cacheService.setCache(categoryService.selectCategories(), key);
            cacheList = (List<ProductDto>) cacheService.getCache(key);
        }

        resultMap = DefaultRes.selectRes(cacheList);

        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));

    }

    /*
     * case 5.
     * ADD / DELETE / UPDATE의 경우 처리 후 Evict 처리를 하고 데이터를 Reload 해서 바뀐 정보를 유지해야하지만
     * '현실에서는 별도의 API를 통하여 일어나는 행위'로 인해 따로 처리하지 않음
     * */

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ResponseEntity insertCategoryApi(@RequestBody CategoryDto categoryDto) throws Exception {

        HashMap<String, Object> resultMap;
        boolean paramFlag = StringUtils.isEmpty(categoryDto.getCategory_name()) || StringUtils.isEmpty(categoryDto.getDepth());

        if (!paramFlag) {
            int successRow = categoryService.insertCategory(categoryDto);
            resultMap = DefaultRes.updateRes(successRow);
        } else {
            resultMap = DefaultRes.badRequest();
        }
        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));
    }

    @RequestMapping(value = "/category", method = RequestMethod.PUT)
    public ResponseEntity updateCategoryApi(@RequestBody CategoryDto categoryDto) throws Exception {
        HashMap<String, Object> resultMap;
        boolean paramFlag = StringUtils.isEmpty(categoryDto.getCategory_no()) ? true
                : StringUtils.isEmpty(categoryDto.getCategory_name()) && StringUtils.isEmpty(categoryDto.getParent_no()) && StringUtils.isEmpty(categoryDto.getDepth());

        if (!paramFlag) {
            int successRow = categoryService.updateCategory(categoryDto);
            resultMap = DefaultRes.updateRes(successRow);
        } else {
            resultMap = DefaultRes.badRequest();
        }

        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));

    }

    @RequestMapping(value = "/category", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategoryApi(@RequestBody CategoryDto categoryDto) throws Exception {

        HashMap<String, Object> resultMap;
        boolean paramFlag = StringUtils.isEmpty(categoryDto.getCategory_no());

        if (!paramFlag) {
            int successRow = categoryService.deleteCategory(categoryDto);
            resultMap = DefaultRes.updateRes(successRow);
        } else {
            resultMap = DefaultRes.badRequest();
        }

        return new ResponseEntity(resultMap.get("defaultRes"), (org.springframework.http.HttpStatus) resultMap.get("HttpStatus"));
    }

}

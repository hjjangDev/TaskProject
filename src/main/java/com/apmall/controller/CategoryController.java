package com.apmall.controller;

import com.apmall.constant.ResponseMessage;
import com.apmall.constant.StatusCode;
import com.apmall.dto.category.CategoryDto;
import com.apmall.dto.defaultRes.DefaultRes;
import com.apmall.service.cache.CacheService;
import com.apmall.service.category.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CacheService cacheService;

    public CategoryController(CategoryService categoryService, CacheService cacheService) {
        this.categoryService = categoryService;
        this.cacheService = cacheService;
    }

    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public ResponseEntity categoryListApi() throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SUCCESS, cacheService.getCacheItemList(null, "category")), HttpStatus.OK);
    }

    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public ResponseEntity insertCategoryApi(@RequestBody CategoryDto categoryDto) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED, ResponseMessage.SUCCESS, categoryService.insertCategory(categoryDto)), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/category/update", method = RequestMethod.PUT)
    public ResponseEntity updateCategoryApi(@RequestBody CategoryDto categoryDto) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SUCCESS, categoryService.updateCategory(categoryDto)), HttpStatus.OK);
    }

    @RequestMapping(value = "/category/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategoryApi(@RequestBody CategoryDto categoryDto) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SUCCESS, categoryService.deleteCategory(categoryDto)), HttpStatus.OK);
    }

}

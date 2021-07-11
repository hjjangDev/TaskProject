package com.apmall.controller;

import com.apmall.dao.ApmDAO;
import com.apmall.dto.CategoryDto;
import com.apmall.dto.ProductDto;
import com.apmall.service.CacheService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class TaskController {

    @Autowired
    private CacheService cacheService;

    @Autowired
    @Qualifier("apmDAO")
    private ApmDAO apmDAO;

    @PostConstruct //프로젝트 실행시 처음 한번 실행하여 모든 목록 불러오기
    public void init() throws Exception {
        cacheService.getCategoryList();
        cacheService.getAllProductList(null);
    }

    @RequestMapping(value="/category/list", method= RequestMethod.POST)
    public String categoryListApi() throws Exception {
        List<CategoryDto> categoryList = cacheService.getCategoryList();

        return  new Gson().toJson(categoryList);
    }

    @RequestMapping(value="/product/list", method= RequestMethod.POST)
    public String productListApi(@RequestBody ProductDto productDto) throws Exception {
        List<ProductDto> categoryList = cacheService.getAllProductList(productDto);

        return  new Gson().toJson(categoryList);
    }

    @RequestMapping(value="/category/add", method= RequestMethod.POST)
    public String insertCategoryApi(@RequestBody Map<String, String> paramMap) throws Exception {

        return new Gson().toJson( apmDAO.insertCategory(paramMap));
    }

    @RequestMapping(value="/category/update", method= RequestMethod.POST)
    public String updateCategoryApi(@RequestBody Map<String, String> paramMap) throws Exception {
        return  new Gson().toJson(apmDAO.updateCategory(paramMap));
    }

    @RequestMapping(value="/category/delete", method= RequestMethod.POST)
    public String deleteCategoryApi(@RequestBody Map<String, String> paramMap) throws Exception {
        return  new Gson().toJson(apmDAO.deleteCategory(paramMap));
    }

    @RequestMapping(value="/product/add", method= RequestMethod.POST)
    public String insertProductApi(@RequestBody Map<String, String> paramMap) throws Exception {
        return  new Gson().toJson(apmDAO.insertProduct(paramMap));
    }

    @RequestMapping(value="/product/update", method= RequestMethod.POST)
    public String updateProductApi(@RequestBody Map<String, String> paramMap) throws Exception {
        return  new Gson().toJson(apmDAO.updateProduct(paramMap));
    }

    @RequestMapping(value="/product/delete", method= RequestMethod.POST)
    public String deleteProductApi(@RequestBody Map<String, String> paramMap) throws Exception {
        return  new Gson().toJson(apmDAO.deleteProduct(paramMap));
    }
}

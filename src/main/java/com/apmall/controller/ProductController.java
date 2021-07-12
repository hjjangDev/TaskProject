package com.apmall.controller;

import com.apmall.constant.ResponseMessage;
import com.apmall.constant.StatusCode;
import com.apmall.dao.product.ProductDAO;
import com.apmall.dto.defaultRes.DefaultRes;
import com.apmall.dto.product.ProductDto;
import com.apmall.service.cache.CacheService;
import com.apmall.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ProductController{

    private final ProductService productService;
    private final CacheService cacheService;

    public ProductController(ProductService productService, CacheService cacheService) {
        this.productService = productService;
        this.cacheService = cacheService;
    }

    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public ResponseEntity allProductListApi(@RequestParam Map<String, String> paramMap) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SUCCESS, cacheService.getCacheItemList(paramMap, "product")), HttpStatus.OK);
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    public ResponseEntity insertProductApi(@RequestBody ProductDto productDto) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED , ResponseMessage.SUCCESS,  productService.insertProduct(productDto)), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/product/update", method = RequestMethod.PUT)
    public ResponseEntity updateProductApi(@RequestBody ProductDto productDto) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.OK , ResponseMessage.SUCCESS,  productService.updateProduct(productDto)), HttpStatus.OK);
    }

    @RequestMapping(value = "/product/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteProductApi(@RequestBody ProductDto productDto) throws Exception {
        return new ResponseEntity(DefaultRes.res(StatusCode.OK , ResponseMessage.SUCCESS,  productService.deleteProduct(productDto)), HttpStatus.OK);
    }
}

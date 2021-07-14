package com.apmall.service.product;

import com.apmall.dto.product.ProductDto;

import java.util.HashMap;
import java.util.List;

public interface ProductService {

    List<ProductDto> selectProducts(HashMap<String, String> paramMap) throws Exception;

    int insertProduct(ProductDto productDto) throws Exception;

    int updateProduct(ProductDto productDto) throws Exception;

    int deleteProduct(ProductDto productDto) throws Exception;

}

package com.apmall.service.product;

import com.apmall.dto.product.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<ProductDto> selectProducts(Map<String, String> paramMap) throws Exception;

    int insertProduct(ProductDto productDto) throws Exception;

    int updateProduct(ProductDto productDto) throws Exception;

    int deleteProduct(ProductDto productDto) throws Exception;

}

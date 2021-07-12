package com.apmall.service.product.impl;

import com.apmall.dao.product.ProductDAO;
import com.apmall.dto.product.ProductDto;
import com.apmall.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<ProductDto> selectProducts(Map<String, String> paramMap) throws Exception {
        return productDAO.selectProducts(paramMap);
    }

    @Override
    public int insertProduct(ProductDto productDto) throws Exception {
        return productDAO.insertProduct(productDto);
    }

    @Override
    public int updateProduct(ProductDto productDto) throws Exception {
        return productDAO.updateProduct(productDto);
    }

    @Override
    public int deleteProduct(ProductDto productDto) throws Exception {
        return productDAO.deleteProduct(productDto);
    }

}

package com.apmall.dao.category;

import com.apmall.dto.category.CategoryDto;
import com.apmall.dto.product.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("categoryDAO")
public interface CategoryDAO {

    List<CategoryDto> selectCategories() throws Exception;

    int insertCategory(CategoryDto categoryDto) throws Exception;

    int updateCategory(CategoryDto categoryDto) throws Exception;

    int deleteCategory(CategoryDto categoryDto) throws Exception;

}

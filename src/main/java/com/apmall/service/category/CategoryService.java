package com.apmall.service.category;

import com.apmall.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> selectCategories() throws Exception;

    int insertCategory(CategoryDto categoryDto) throws Exception;

    int updateCategory(CategoryDto categoryDto) throws Exception;

    int deleteCategory(CategoryDto categoryDto) throws Exception;

}

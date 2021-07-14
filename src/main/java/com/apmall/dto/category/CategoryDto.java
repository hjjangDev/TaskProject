package com.apmall.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CategoryDto {

    public String category_no;
    public String category_name;
    public String parent_no;
    public String depth;

}

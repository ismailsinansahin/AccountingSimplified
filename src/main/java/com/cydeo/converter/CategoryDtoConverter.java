package com.cydeo.converter;

import com.cydeo.dto.CategoryDto;
import com.cydeo.service.CategoryService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoConverter  implements Converter<String, CategoryDto> {

    private final CategoryService categoryService;

    public CategoryDtoConverter(@Lazy CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@SneakyThrows
    @Override
    public CategoryDto convert(String id){
        if (id==null ||id.isBlank()){
            return null;
        }
        return categoryService.findCategoryById(Long.parseLong(id));
    }

}

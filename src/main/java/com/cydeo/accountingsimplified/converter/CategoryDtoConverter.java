package com.cydeo.accountingsimplified.converter;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.service.CategoryService;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class CategoryDtoConverter  implements Converter<String, CategoryDto> {

    private final CategoryService categoryService;

    public CategoryDtoConverter(@Lazy CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @SneakyThrows
    @Override
    public CategoryDto convert(String id){
        return categoryService.findCategoryById(Long.parseLong(id));
    }

}

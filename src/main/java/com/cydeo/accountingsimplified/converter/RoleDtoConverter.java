package com.cydeo.accountingsimplified.converter;

import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.service.RoleService;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class RoleDtoConverter implements Converter<String, RoleDto> {

    private final RoleService roleService;

    public RoleDtoConverter(@Lazy RoleService roleService) {
        this.roleService = roleService;
    }

//    @SneakyThrows
    @Override
    public RoleDto convert(String id){
        // it throws error if user selects "Select" even with @SneakyThrows
        if (id == null || id.isBlank())
            return null;
        return roleService.findRoleById(Long.parseLong(id));
    }

}
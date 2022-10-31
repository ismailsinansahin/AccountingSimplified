package com.cydeo.accountingsimplified.converter;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class InvoiceProductDtoConverter implements Converter<String, InvoiceProductDto> {
    private final InvoiceProductService invoiceProductService;

    public InvoiceProductDtoConverter(@Lazy InvoiceProductService invoiceProductService) {

        this.invoiceProductService = invoiceProductService;
    }

    @SneakyThrows
    @Override
    public InvoiceProductDto convert(String id){
        return invoiceProductService.findInvoiceProductById(Long.parseLong(id));
    }

}

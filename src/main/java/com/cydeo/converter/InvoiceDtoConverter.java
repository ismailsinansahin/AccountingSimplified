package com.cydeo.converter;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.service.InvoiceService;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class InvoiceDtoConverter implements Converter<String, InvoiceDto> {

    private final InvoiceService invoiceService;

    public InvoiceDtoConverter(@Lazy InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @SneakyThrows
    @Override
    public InvoiceDto convert(String id){
        return invoiceService.findInvoiceById(Long.parseLong(id));
    }

}

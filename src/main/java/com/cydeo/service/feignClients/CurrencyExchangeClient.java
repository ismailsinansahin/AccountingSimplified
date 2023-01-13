package com.cydeo.service.feignClients;


import com.cydeo.dto.common.CurrencyApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;

@FeignClient(name = "currency-api", url = "${currency.api.url}")
public interface CurrencyExchangeClient {

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    CurrencyApiResponse getUsdBasedCurrencies();
}

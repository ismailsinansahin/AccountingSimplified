package com.cydeo.service.feignClients;


import com.cydeo.dto.common.Country;
import com.cydeo.dto.common.State;
import com.cydeo.dto.common.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;

@FeignClient(name = "address-api", url = "${address.api.url}")
public interface AddressFeignClient {

    @GetMapping(value = "/getaccesstoken", consumes = MediaType.APPLICATION_JSON_VALUE)
    TokenDto auth(@RequestHeader("user-email") String email, @RequestHeader("api-token") String apiToken );

    @GetMapping(value = "/countries", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Country> getCountryList(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(value = "/states/{country}", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<State> getStateList(@RequestHeader("Authorization") String bearerToken,@PathVariable String country);

    @GetMapping(value = "/cities/{state}", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<State> getCityList(@RequestHeader("Authorization") String bearerToken,@PathVariable String state);
}

package com.cydeo.accountingsimplified;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class AccountingSimplifiedApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountingSimplifiedApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate getRestTemplate() {

        var factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(8000);
        factory.setReadTimeout(8000);
        return new RestTemplate(factory);
    }

    @Bean
    public WebClient getWebCleint(){
        return  WebClient.create();
    }

}

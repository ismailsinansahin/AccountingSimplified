package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.annotation.AccountingExceptionMessage;
import com.cydeo.accountingsimplified.exception.AccountingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionPractice {

    @GetMapping("/1")
    public void custom(){
        throw new AccountingException("custom exception");
    }

    @GetMapping("/2")
    public void library(){
        throw new RuntimeException("library exception");
    }

    @AccountingExceptionMessage(defaultMessage = "test only me")
    @GetMapping("/3")
    public void library2(){
        throw new NullPointerException();
    }

    @AccountingExceptionMessage(defaultMessage = "test both")
    @GetMapping("/4")
    public void library3() throws Exception {
        throw new Exception("from exception");
    }

    @GetMapping("/5")
    public String thymeleaf() {
        return "/aaa";
    }

}

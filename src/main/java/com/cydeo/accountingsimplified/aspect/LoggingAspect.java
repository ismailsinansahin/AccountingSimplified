package com.cydeo.accountingsimplified.aspect;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.service.SecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggingAspect {

    private final SecurityService securityService;

    @Pointcut("execution(* com.cydeo.accountingsimplified.service.CompanyService.activate(..)) " +
            "|| execution(* com.cydeo.accountingsimplified.service.CompanyService.deactivate(..))")
    public void anyCompanyActivationDeactivation() {
    }

    @AfterReturning(value = "anyCompanyActivationDeactivation()", returning = "dto")
    public void afterAnyCompanyActivation(JoinPoint joinPoint, CompanyDto dto) {
        log.info("Method: {}, Company Name:{}, User -> FirstName: {}, LastName: {}, Username: {}",
                joinPoint.getSignature().toShortString(),
                dto.getTitle(),
                securityService.getLoggedInUser().getFirstname(),
                securityService.getLoggedInUser().getLastname(),
                securityService.getLoggedInUser().getUsername());
    }

}

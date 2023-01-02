package com.cydeo.aspect;

import com.cydeo.dto.CompanyDto;
import com.cydeo.service.SecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggingAspect {

    private final SecurityService securityService;

    // catch if CompanyService activate() or deactivate() method is executed
    // (..) : method parameters are not important
    @Pointcut("execution(* com.cydeo.service.CompanyService.activate(..)) " +
            "|| execution(* com.cydeo.service.CompanyService.deactivate(..))")
    public void anyCompanyActivationDeactivation() {
    }

    // if anyCompanyActivationDeactivation() method (above) is executed,
    // log the method name (activate / deactivate), company name of the activated / deactivated,
    // firstname, lastname and username of the logged-in user after that method executed
    @AfterReturning(value = "anyCompanyActivationDeactivation()", returning = "dto")
    public void afterAnyCompanyActivation(JoinPoint joinPoint, CompanyDto dto) {
        log.info("Method: {}, Company Name:{}, User -> FirstName: {}, LastName: {}, Username: {}",
                joinPoint.getSignature().toShortString(),
                dto.getTitle(),
                securityService.getLoggedInUser().getFirstname(),
                securityService.getLoggedInUser().getLastname(),
                securityService.getLoggedInUser().getUsername());
    }

    @Pointcut("execution(* com.cydeo..*(..))")
    private void anyRuntimeException() {
    }

    @AfterThrowing(pointcut = "anyRuntimeException()", throwing = "exception")
    public void afterThrowingControllerAdvice(JoinPoint joinPoint, RuntimeException exception){
        log.error("After Throwing -> Method: {} - Exception: {} - Message: {}", joinPoint.getSignature().toShortString(), exception.getClass().getSimpleName(), exception.getMessage());
    }
}

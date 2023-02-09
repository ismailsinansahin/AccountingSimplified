package com.cydeo.accountingsimplified.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.cydeo.accountingsimplified.controller.*.*(..))")
    public void controllerPointCut() {
    }

    @Pointcut("execution(* com.cydeo.accountingsimplified.service.*.*(..)) && !execution(* com.cydeo.accountingsimplified.service.SecurityService.*(..))")
    public void servicePointCut() {
    }

    @Before("controllerPointCut()")
    public void controllerAdvice(JoinPoint joinPoint) {
        log.info("[Controller Operation]: User-> {}, Method-> {}, Parameters-> {}",
                "manager@greentech.com", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @Before("servicePointCut()")
    public void serviceAdvice(JoinPoint joinPoint) {
        log.info("[Service Operation]: User-> {}, Method-> {}, Parameters-> {}", "manager@greentech.com", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "controllerPointCut() || servicePointCut()", throwing = "exception")
    public void throwingAdvice(JoinPoint joinPoint, Exception exception) {
        log.info("[!!!Exception Thrown!!!]: User-> {}, Method-> {}, Parameters-> {}, Exception-> {}", "manager@greentech.com", joinPoint.getSignature().toShortString(), joinPoint.getArgs(), exception.getMessage());
    }

    @Around("controllerPointCut()")
    public Object performanceLoggingAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long beforeOperation = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long afterOperation = System.currentTimeMillis();
        long operationTime = afterOperation - beforeOperation;
        log.info("[Performance Log]: Execution Time-> {} ms, User-> {}, Operation-> {}", operationTime, "manager@greentech.com", proceedingJoinPoint.getSignature().toShortString());
        return result;
    }

}
package com.cydeo.accountingsimplified.exception;

import com.cydeo.accountingsimplified.annotation.AccountingExceptionMessage;
import com.cydeo.accountingsimplified.dto.DefaultExceptionMessageDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    // whenever TicketingProjectException throws, run this method
    @ExceptionHandler(AccountingException.class)
    public String serviceException(AccountingException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    // whenever one of these exception throws, run this method and show this ResponseEntity
    // if there is our custom annotation at any method, it'll build the response including that message,
    // otherwise it'll show "Something went wrong!" message
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class, BadCredentialsException.class})
    public String genericException(Throwable exception, Model model, HandlerMethod handlerMethod) {
        String message = "Something went wrong!";
        Optional<DefaultExceptionMessageDto> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent()) {
            message = defaultMessage.get().getMessage();
        } else if (!exception.getMessage().isEmpty()) {
            message = exception.getMessage();
        }
        model.addAttribute("message", message);
        return "error";
    }

    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(Method method) {
        AccountingExceptionMessage defaultExceptionMessage = method
                .getAnnotation(AccountingExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDto defaultExceptionMessageDto = DefaultExceptionMessageDto
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDto);
        }
        return Optional.empty();
    }
}
package com.cydeo.accountingsimplified.app_util;

import org.springframework.validation.BindingResult;

/**
 * This class is created for generating a validation error when needed.
 */
public class ErrorGenerator {

    /**
     *
     * @param bindingResult will provide rejectValue() method.
     * @param field will correspond to the field of relevant dto
     * @param errorMessage you can customize your error message
     */
    public static void generateErrorMessage(BindingResult bindingResult, String field, String errorMessage) {
        bindingResult.rejectValue(field, " ", errorMessage);
    }
}

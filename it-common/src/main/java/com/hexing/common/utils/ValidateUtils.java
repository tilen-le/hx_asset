package com.hexing.common.utils;

import com.hexing.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 80010641
 * 参数校验
 */
public class ValidateUtils {

    public static void validAll(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String errorMessage = allErrors.stream()
                    .filter(e -> StringUtils.isNotBlank(e.getDefaultMessage()))
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(","));
            throw new ServiceException(errorMessage);
        }
    }

}

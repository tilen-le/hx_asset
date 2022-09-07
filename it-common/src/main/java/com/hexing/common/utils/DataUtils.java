package com.hexing.common.utils;



import com.hexing.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 入参校验工具类
 */
public class DataUtils {

    public static void validBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String errorMessage = allErrors.stream()
                    .filter(e -> StringUtils.isNotBlank(e.getDefaultMessage()))
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(","));
            throw new ServiceException(errorMessage);
        }
    }

    public static void nullException(Object obj) {
        if (Objects.isNull(obj)) {
            throw new ServiceException(MessageUtils.message("common.data.lack"));
        }
    }

    public static void nullException(Object obj, String errorMsg) {
        if (Objects.isNull(obj)) {
            throw new ServiceException(errorMsg);
        }
    }

    public static void blankException(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(errorMsg);
        }
    }

    public static void blankException(String str) {
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(MessageUtils.message("common.parameter.lack"));
        }
    }

    public static void emptyException(Object obj) {
        if (Objects.isNull(obj)) {
            throw new ServiceException(MessageUtils.message("common.parameter.lack"));
        }
    }

//    public static LocalDateTime dateToLocal(Date date) {
//        return LocalDateTimeUtil.of(date);
//    }

    public static Date localToDate(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static Date localToDate(LocalDateTime localDateTime) {
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static String escapeChar(String res) {
        StringBuilder strDest = new StringBuilder();
        for (int i = 0; i < res.length(); i++) {
            char ch = res.charAt(i);
            switch (ch) {
                case '\'':
                    strDest.append("\\\'");
                    break;
                case '"':
                    strDest.append("\\\"");
                    break;
                case '\\':
                    strDest.append("\\\\\\\\");
                    break;
                case '%':
                    strDest.append("\\%");
                    break;
                case '_':
                    strDest.append("\\_");
                    break;
                default:
                    strDest.append(ch);
                    break;
            }
        }
        return strDest.toString();
    }


    public static String getResourceXml(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String str = "";
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  sb.toString();
    }

}

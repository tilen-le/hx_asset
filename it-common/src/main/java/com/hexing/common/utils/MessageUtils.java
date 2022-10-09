package com.hexing.common.utils;

import com.hexing.common.utils.spring.SpringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 获取i18n资源文件
 *
 * @author ruoyi
 */
public class MessageUtils {
    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        Locale locale = null;
        String language = getLanguage();
        if (StringUtils.isNotBlank(language)) {
            //手动或者自动对应
            if ("en".equals(language)) {
                locale = Locale.US;
            } else if ("zh".equals(language)) {
                locale = Locale.SIMPLIFIED_CHINESE;
            } else {
                locale = LocaleContextHolder.getLocale();
            }
        } else {
            locale = LocaleContextHolder.getLocale();
        }
        return messageSource.getMessage(code, args, locale);
    }

    public static String getLanguage() {
        //从cookie中获取前端选择的语言
        HttpServletRequest request = ServletUtils.getRequest();
        Cookie[] cookies = request.getCookies();
        //判断来源是否是外部链接,外部链接语言和系统语言隔离
        String referer = request.getHeader("Referer");
        Boolean link = Boolean.FALSE;
        if (StringUtils.isNotBlank(referer)) {
            link = referer.contains("/form/extend") || referer.contains("/form/warranty");
        }
        String key = "language";
        if (link) {
            key = "link_language";
        }
        String language = "";
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    language = cookie.getValue();
                    break;
                }
            }
        }
        // tip：后台默认语言
        if (StringUtils.isBlank(language)) {
            language = "zh";
        }
        return language;
    }
}

package com.mouchina.web.base.utils;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * Description: MessageSourceHelper
 * Author: ourufeng
 * Update: ourufeng(2015-05-08 14:32)
 */
public class MessageSourceHelper {

    private ReloadableResourceBundleMessageSource messageSource;

    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(Integer code) {
        return getMessage(code, null, null, Locale.CHINA);
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, null, null);
    }

    public String getMessage(Integer code, Object[] args, String defaultMessage, Locale locale) {
        String msg = messageSource.getMessage(code.toString(), args, defaultMessage, locale);
        return msg != null ? msg.trim() : msg;
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String msg = messageSource.getMessage(code, args, defaultMessage, locale);
        return msg != null ? msg.trim() : msg;
    }
}

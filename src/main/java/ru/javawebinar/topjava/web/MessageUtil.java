package ru.javawebinar.topjava.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {
    private final MessageSource messageSource;

    public static final Locale RU_LOCALE = new Locale("ru");

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String message, Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }

    public String getMessage(String message) {
        return getMessage(message, LocaleContextHolder.getLocale());
    }


}

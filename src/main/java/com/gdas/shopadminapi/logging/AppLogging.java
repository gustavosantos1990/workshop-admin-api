package com.gdas.shopadminapi.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdas.shopadminapi.dtos.AppLog;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.gdas.shopadminapi.utils.DateTimeUtils.currentLocalDateTime;

@Aspect
@Component
public class AppLogging {

    Logger logger = LoggerFactory.getLogger(AppLogging.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;

    public AppLogging(ObjectMapper objectMapper, HttpServletRequest httpServletRequest) {
        this.objectMapper = objectMapper;
        this.httpServletRequest = httpServletRequest;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Around("restControllerMethods()")
    public Object aroundRestControllerMethods(ProceedingJoinPoint pjp) throws Throwable {
        AppLog appLog = new AppLog(currentLocalDateTime());

        try {

            appLog.setUri(httpServletRequest.getRequestURI());
            appLog.setHttpMethod(httpServletRequest.getMethod());
            appLog.setHeaders(resolveHeaders());
            appLog.setArgs(resolveArgs(pjp));

            Object result = pjp.proceed(pjp.getArgs());
            appLog.setResponse(result);

            appLog.setEnd(currentLocalDateTime());
            appLog.setElapsed(appLog.getEnd().compareTo(appLog.getStart()));

            logger.info(objectMapper.writeValueAsString(appLog));

            return result;
        } catch (Throwable exception) {

            appLog.setErrorType(exception.getClass());
            appLog.setStackTrace(ExceptionUtils.getStackTrace(exception));
            appLog.setErrorMessage(exception.getMessage());

            appLog.setEnd(currentLocalDateTime());
            appLog.setElapsed(appLog.getEnd().compareTo(appLog.getStart()));

            String log = logToString(appLog);

            logger.error(log);

            throw exception;
        }
    }

    private String logToString(AppLog appLog) {
        try {
            return objectMapper.writeValueAsString(appLog);
        } catch (JsonProcessingException e) {
            return String.valueOf(appLog);
        }
    }

    private Map<String, Object> resolveArgs(ProceedingJoinPoint pjp) {
        Map<String, Object> result = new HashMap<>();
        CodeSignature codeSignature = (CodeSignature) pjp.getSignature();
        for (int i = 0; i < pjp.getArgs().length; i++) {
            result.put(codeSignature.getParameterNames()[i], pjp.getArgs()[i]);
        }
        return result;
    }

    private Map<String, String> resolveHeaders() {
        Map<String, String> result = new HashMap<>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            result.put(header, httpServletRequest.getHeader(header));
        }
        return result;
    }

}

package com.sxkl.cloudnote.log.aop;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.log.entity.LogLevel;
import com.sxkl.cloudnote.log.service.LogService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.IPUtils;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.RequestUtils;
import com.sxkl.cloudnote.utils.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogInterceptor {

    @Autowired
    private LogService logService;

    @Around("@annotation(logger)")
    public Object afterMethod(ProceedingJoinPoint pjp, Logger logger) throws Throwable {
        Log log = configurateLog(pjp);
        LogLevel logLevel = LogLevel.valueOf(LogLevel.class, logger.logLevel());
        long start = System.currentTimeMillis();
        Object object = pjp.proceed();
        long end = System.currentTimeMillis();
        long costTimeTemp = end - start;
        int costTime = NumberUtils.toInt(costTimeTemp + "");
        log.setLogLevel(logLevel);
        log.setCostTime(costTime);
        logService.showLogInConsole(log);
        return object;
    }

    @AfterThrowing(pointcut = "execution(* com.sxkl.cloudnote.*.service.*.*(..))", throwing = "e")
    public void doServiceAfterThrowing(JoinPoint jp, Throwable e) {
        Log log = configurateLog(jp);
        if (ObjectUtils.isNull(log)) {
            return;
        }
        log.setLogLevel(LogLevel.ERROR);
        log.setErrorMsg(e.getMessage());
        log.setThrowable(e);
        logService.showLogInConsole(log);
    }

    @AfterThrowing(pointcut = "execution(* com.sxkl.cloudnote.*.controller.*.*(..))", throwing = "e")
    public void doControllerAfterThrowing(JoinPoint jp, Throwable e) {
        Log log = configurateLog(jp);
        log.setLogLevel(LogLevel.ERROR);
        log.setErrorMsg(e.getMessage());
        log.setThrowable(e);
        logService.showLogInConsole(log);
    }

    private Log configurateLog(JoinPoint jp) {
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        if (methodName.contains("onMessage")) {
            return null;
        }
        String className = jp.getTarget().getClass().getName();
        String message = getServiceMethodDescription(method);
        Date happenTime = new Date();
        Log log = new Log();
        log.setClassName(className);
        log.setMethodName(methodName);
        log.setMessage(message);
        log.setDate(happenTime);
        HttpServletRequest request = RequestUtils.getRequest();
        if (request != null) {
            log.setIp(IPUtils.getIPAddr(request));
            User user = UserUtil.getSessionUser(request);
            if (user != null) {
                log.setUserId(user.getId());
                log.setUserName(user.getName());
            }
        }
        return log;
    }

    private String getServiceMethodDescription(Method method) {
        String methodDescription = method.getName();
        try {
            Logger logger = method.getAnnotation(Logger.class);
            methodDescription = logger.message();
        } catch (Exception e) {
        }
        return methodDescription;
    }
}

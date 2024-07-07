package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.MessageService.processMessage(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Before processing message");
        Object result = joinPoint.proceed();
        logger.info("After processing message");
        return result;
    }
}

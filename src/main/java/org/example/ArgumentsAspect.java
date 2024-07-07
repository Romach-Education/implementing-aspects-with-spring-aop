package org.example;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class ArgumentsAspect {
    private Logger logger = Logger.getLogger(ArgumentsAspect.class.getName());

    @Before("@annotation(ArgumentsLog)")
    public void logArguments(JoinPoint joinPoint) {
        final String methodName = joinPoint.getSignature().getName();
        final Object [] arguments = joinPoint.getArgs();
        logger.info("Method " + methodName + " with parameters " + Arrays.asList(arguments) + " will execute");
    }
}

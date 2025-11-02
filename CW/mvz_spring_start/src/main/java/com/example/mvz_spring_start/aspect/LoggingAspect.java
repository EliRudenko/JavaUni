package com.example.mvz_spring_start.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.mvz_spring_start.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("→ Виклик методу: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.example.mvz_spring_start.service.*.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("← Метод " + joinPoint.getSignature().toShortString() + " завершено. Результат: " + result);
    }
}

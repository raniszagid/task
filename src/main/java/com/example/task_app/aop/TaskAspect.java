package com.example.task_app.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class TaskAspect {
    @Pointcut("@within(com.example.task_app.aop.annotation.ExecutionDuration)")
    public void joinPointsArea() {}

    @Around("joinPointsArea()")
    public Object measureMethodExecution(ProceedingJoinPoint joinPoint) {
        Object result = null;
        long start = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        long end = System.currentTimeMillis();
        long executionDuration = end - start;
        log.info("{} method execution: {} ms", joinPoint.getSignature().getName(), executionDuration);
        return result;
    }
}

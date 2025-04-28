package com.example.task_app.aop;

import com.example.task_app.validation.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@Slf4j
public class TaskAspect {
    @Pointcut("@annotation(com.example.task_app.aop.annotation.ExecutionDuration)")
    public void durationMeasureJoinPoints() {}

    @Pointcut("@annotation(com.example.task_app.aop.annotation.NotFoundErrorCatcher)")
    public void notFoundJoinPoints() {}

    @Pointcut("@annotation(com.example.task_app.aop.annotation.TaskReturnAnnotation)")
    public void afterReturnJoinPoints() {}

    @Pointcut("@annotation(com.example.task_app.aop.annotation.BeforeMethodExecution)")
    public void beforeMethodExecutionJoinPoints() {}

    @Around("durationMeasureJoinPoints()")
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

    @AfterThrowing(
            pointcut = "notFoundJoinPoints()",
            throwing = "exception"
    )
    public void catchNotFoundError(JoinPoint joinPoint, TaskNotFoundException exception) {
        log.error(exception.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @AfterReturning(pointcut = "afterReturnJoinPoints()")
    public void logAfterReturning() {
        log.info("Task (or empty status) returned to client");
    }

    @Before("beforeMethodExecutionJoinPoints()")
    public void logBeforeMethodExecution() {
        log.info("Try to create task");
    }
}

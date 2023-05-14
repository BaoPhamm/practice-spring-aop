package com.bao.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.bao.demo.controller.MyController.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before " + joinPoint.getSignature().getName() + "()");
    }

    @After("execution(void com.bao.demo.controller.MyController.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After " + joinPoint.getSignature().getName() + "()");
    }

    @AfterReturning(
            pointcut = "execution(* com.bao.demo.controller.MyController.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("After " + joinPoint.getSignature().getName() + "() returning " + result);
    }

    // using args in Before
    @Before("execution(* com.bao.demo.controller.MyController.*(..)) && args(param,..)")
    public void logBeforeToGetParamUsingArgs(String param) {
        System.out.println("(args in Before) Request parameter: " + param);
    }

    // using JoinPoint
    @Before("execution(* com.bao.demo.controller.MyController.*(..))")
    public void logBeforeToGetParamUsingJoinPoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                System.out.println("(JoinPoint) Method argument: " + arg);
            }
        }
    }

    @Around("execution(* com.bao.demo.controller.MyController.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Method " + joinPoint.getSignature().getName() + "() execution time: " + duration + " milliseconds");
        return result;
    }

    @Around("execution(* com.bao.demo.service.MyService.returnValueSetByAspect(..))")
    public Object changeReturnValueInService(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        if (methodArgs[0].toString().equals("changeInService")) {
            // modify method argument
            String modifiedArg = methodArgs[0].toString() + " - argModified";
            String result = (String) joinPoint.proceed(new Object[]{modifiedArg});

            String newResult = result.toUpperCase();
            System.out.println("Old return value: " + result);
            System.out.println("New return value: " + newResult);
            return newResult;
        }
        return joinPoint.proceed();
    }

    @Around("execution(* com.bao.demo.controller.MyController.returnValueSetByAspect(..))")
    public Object changeReturnValueInController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        ResponseEntity<String> result = (ResponseEntity) joinPoint.proceed();
        if (methodArgs[0].toString().equals("changeInController") && Objects.nonNull(result.getBody())) {
            String newResultBody = result.getBody().toUpperCase();
            System.out.println("Old return value: " + result.getBody());
            System.out.println("New return value: " + newResultBody);
            return ResponseEntity.ok(newResultBody);
        }
        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.bao.demo.controller.MyController.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        System.out.println("Exception thrown by method: " + joinPoint.getSignature().getName() + "()");
        System.out.println("Exception message: " + ex.getMessage());
    }

}

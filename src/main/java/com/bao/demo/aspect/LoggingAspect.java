package com.bao.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.bao.demo.service.MyService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before " + joinPoint.getSignature().getName() + "()");
    }

    @AfterReturning(
            pointcut = "execution(* com.bao.demo.service.MyService.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;
        if (hasReturnType) {
            System.out.println("After " + joinPoint.getSignature().getName() + "() returning " + result);
        } else {
            System.out.println("After " + joinPoint.getSignature().getName() + "()");
        }
    }

    // using args in Before
    @Before("execution(* com.bao.demo.controller.MyController.*(..)) && args(param,..)")
    public void logBeforeToGetParamUsingArgs(String param) {
        System.out.println("(args in Before) Request parameterrrr: " + param);
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

    @AfterThrowing(pointcut = "execution(* com.bao.demo.controller.MyController.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        System.out.println("Exception thrown by method: " + joinPoint.getSignature().getName() + "()");
        System.out.println("Exception message: " + ex.getMessage());
    }

}

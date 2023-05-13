package com.bao.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.bao.demo.service.MyService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before " + joinPoint.getSignature().getName() + "()");
    }

    @AfterReturning(
            pointcut = "execution(* com.bao.demo.service.MyService.doSomethingElse(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("After " + joinPoint.getSignature().getName() + "() returning " + result);
    }

    // using args in Before
    @Before("execution(* com.bao.demo.controller.MyController.doSomethingElse(..)) && args(param,..)")
    public void logBeforeToGetParamUsingArgs(JoinPoint joinPoint, String param) {
        System.out.println("(args in Before) Request parameterrrr: " + param);
    }

    // using JoinPoint
    @Before("execution(* com.bao.demo.controller.MyController.doSomethingElse(..))")
    public void logBeforeToGetParamUsingJoinPoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                System.out.println("(JoinPoint) Method argument: " + arg);
            }
        }
    }

}

package com.bao.demo.service;

import com.bao.demo.exception.MyException;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    public void doSomething() {
        System.out.println("Doing something...");
    }

    public String doSomethingElse(String param) {
        System.out.println("Doing something else with param: " + param);
        return param.toUpperCase();
    }

    public String returnValueSetByAspect(String param) {
        return "This is return value: " + param;
    }

    public void throwException() {
        System.out.println("Throwing exception.......");
        throw new MyException("oh nooooo");
    }
}

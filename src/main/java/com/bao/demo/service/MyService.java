package com.bao.demo.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {

    public String doSomething() {
        System.out.println("Doing something...");
        return "result of doSomething";
    }

    public String doSomethingElse(String param) {
        System.out.println("Doing something else with param: " + param);
        return "result of doSomething with param: " + param;
    }
}

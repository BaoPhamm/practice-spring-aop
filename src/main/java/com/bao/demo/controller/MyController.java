package com.bao.demo.controller;

import com.bao.demo.service.MyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/do-something")
    public void doSomething() {
        myService.doSomething();
    }

    @GetMapping("/do-something-else")
    public ResponseEntity<String> doSomethingElse(
            @RequestParam(required = false, defaultValue = "") String param) {
        return ResponseEntity.ok(myService.doSomethingElse(param));
    }

    @GetMapping("/throw-exception")
    public ResponseEntity<Void> throwException() {
        myService.throwException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/change-return-val")
    public ResponseEntity<String> returnValueSetByAspect(
            @RequestParam(required = false, defaultValue = "") String param) {
        return ResponseEntity.ok(myService.returnValueSetByAspect(param));
    }
}

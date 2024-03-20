package com.groom.wisebab.controller;

import com.groom.wisebab.service.PromiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PromiseController {

    private final PromiseService promiseService;

    @PostMapping("/promises")
    public String createPromise() {
        return "";
    }
}

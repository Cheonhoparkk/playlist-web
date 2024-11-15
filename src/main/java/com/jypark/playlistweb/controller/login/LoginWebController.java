package com.jypark.playlistweb.controller.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginWebController {

    @GetMapping("/login-view")
    public String loginPage() {
        log.info("Login page accessed");
        return "login/login-view";
    }
}

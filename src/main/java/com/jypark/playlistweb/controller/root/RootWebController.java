package com.jypark.playlistweb.controller.root;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RootWebController {

    @Value("${myapp.page.login-uri}")
    private String returnPageUri;

    @GetMapping("/")
    public String indexPage(final Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 이미 로그인된 사용자는 메인 페이지로 리디렉션
            return "redirect:/main/main-view";
        }
        model.addAttribute("returnPageUri", returnPageUri);
        return "thymeleaf/index";
    }
}

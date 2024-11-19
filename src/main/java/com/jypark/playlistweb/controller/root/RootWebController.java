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

    @Value("${myapp.page.login-uri}")   // application-url.yml에서 설정한 값 가져오기
    private String returnPageUri;

    @GetMapping("/")
    public String indexPage(final Model model, Authentication authentication) { // Authentication : Spring Security가 제공하는 객체, 현재 사용자의 인증 정보를 담고 있음
        if (authentication != null && authentication.isAuthenticated()) { // authentication이 null이면 비로그인상태라는 것, authentication.isAuthenticated()는 사용자가 인증된 상태인지 여부를 boolean 값으로 반환
            // 이미 로그인된 사용자는 메인 페이지로 리디렉션
            return "redirect:/main/main-view";
        }
        model.addAttribute("returnPageUri", returnPageUri);
        return "thymeleaf/index";
    }
}

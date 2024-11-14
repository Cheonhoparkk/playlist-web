package com.jypark.playlistweb.root;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RootWebController {

    @GetMapping("/")
    public String indexPage(final Model model) {
        String returnPageUri = "/login/login-view"; // yml로 뺄 예정

        model.addAttribute("returnPageUri", returnPageUri);
        return "index";
    }
}

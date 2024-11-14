package com.jypark.playlistweb.root;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RootWebController {

    @Value("${myapp.page.login-uri}")
    private String returnPageUri;

    @GetMapping("/")
    public String indexPage(final Model model) {
        model.addAttribute("returnPageUri", returnPageUri);
        return "index";
    }
}

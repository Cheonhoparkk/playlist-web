package com.jypark.playlistweb.controller.main;

import com.jypark.playlistweb.service.youtube.YouTubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/main")
public class MainWebController {

    private final YouTubeService youTubeService;

    @Autowired
    public MainWebController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    /**
     * 메인 페이지에서 YouTube 재생목록을 표시하는 메서드
     * @param model Spring MVC 모델 객체로, 뷰에 데이터를 전달하는 데 사용
     * @param authorizedClient Google 인증된 OAuth2 클라이언트 (로그인된 사용자 정보 포함)
     * @return main/main-view 페이지로 이동
     */
    @GetMapping("/main-view")
    public String mainPage(Model model, @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        // YouTube 재생목록 데이터 가져오기
        String playlists = youTubeService.getPlaylists(authorizedClient).block();   // 비동기 Mono를 동기로 변환

        if (playlists != null) {
            // 재생목록 데이터가 정상적으로 수신된 경우 로그 출력
            log.info("Playlists Data: {}", playlists); // 서버에서 데이터를 확인
            model.addAttribute("playlists", playlists); // 모델에 추가
        } else {
            // 재생목록 데이터가 없는 경우 로그 경고 메시지 출력
            log.warn("No playlists data received.");
            model.addAttribute("playlists", "[]"); // 기본값으로 빈 배열 전달
        }
        return "main/main-view";
    }
}

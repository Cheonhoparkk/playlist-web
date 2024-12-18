package com.jypark.playlistweb.controller.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jypark.playlistweb.service.youtube.YouTubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/main")
public class MainWebController {

    private final YouTubeService youTubeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MainWebController(YouTubeService youTubeService, ObjectMapper objectMapper) {
        this.youTubeService = youTubeService;
        this.objectMapper = objectMapper;
    }

    /**
     * 메인 페이지에서 YouTube 재생목록을 표시하는 메서드
     * @param model Spring MVC 모델 객체로, 뷰에 데이터를 전달하는 데 사용
     * @param authorizedClient Google 인증된 OAuth2 클라이언트 (로그인된 사용자 정보 포함)
     * @return main/main-view 페이지로 이동
     */
    @GetMapping("/main-view")
    public String mainPage(Model model, @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        try {
            // YouTube API에서 재생목록 가져오기
            String playlistsJson = youTubeService.getPlaylists(authorizedClient).block(); // 비동기 Mono를 동기로 변환
            // 재생목록 데이터를 저장할 리스트
            List<Map<String, String>> playlists = new ArrayList<>();

            if (playlistsJson != null) {
                // JSON 데이터를 파싱하여 "items" 배열 추출
                JsonNode items = objectMapper.readTree(playlistsJson).get("items");
                if (items != null) {
                    for (JsonNode item : items) {
                        // 재생목록 ID 추출
                        String id = item.get("id").asText();
                        // 재생목록 제목
                        String name = item.get("snippet").get("title").asText();

                        // 재생목록 데이터를 Map으로 저장하고 리스트에 추가
                        playlists.add(Map.of("id", id, "name", name));
                    }
                }
                log.info("Playlists Data: {}", playlists);
            } else {
                log.warn("No playlists data received.");
            }

            // 모델에 재생목록 데이터 전달
            model.addAttribute("playlists", playlists);
        } catch (Exception e) {
            log.error("Error while fetching or parsing playlists", e);

            // 에러가 발생한 경우에도 빈 리스트를 모델에 추가하여 뷰가 정상적으로 렌더링되도록 함
            model.addAttribute("playlists", List.of());
        }

        return "thymeleaf/main/main-view";
    }
}

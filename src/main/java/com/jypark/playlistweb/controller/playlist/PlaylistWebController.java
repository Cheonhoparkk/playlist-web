package com.jypark.playlistweb.controller.playlist;

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

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/playlist")
public class PlaylistWebController {

    private final YouTubeService youTubeService;

    @Autowired
    public PlaylistWebController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    /**
     * 특정 플레이리스트의 곡 목록을 가져와서 뷰로 전달하는 메서드
     * @param playlistId 요청된 플레이리스트의 ID
     * @param model Spring MVC 모델 객체로, 뷰에 데이터를 전달하는 데 사용
     * @param authorizedClient Google 인증된 OAuth2 클라이언트 (로그인된 사용자 정보 포함)
     * @return playlist/playlist-items 페이지로 이동
     */
    @GetMapping("/{playlistId}")
    public String getPlaylistItems(@PathVariable String playlistId, Model model, @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient){
        // YouTube API를 통해 플레이리스트의 곡 목록 데이터를 가져옴
        String playlistItemsJson = youTubeService.getPlaylistItems(playlistId, authorizedClient).block();

        // JSON 데이터를 객체로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> playlistItems;
        try {
            playlistItems = mapper.readValue(playlistItemsJson, Map.class);
            model.addAttribute("playlistItems", playlistItems); // Thymeleaf에 전달
        } catch (Exception e) {
            log.error("Failed to parse playlist items JSON", e);
            model.addAttribute("playlistItems", Map.of("items", List.of())); // 기본값으로 빈 리스트 전달
        }

        return "playlist/playlist-view";
    }
}

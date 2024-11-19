package com.jypark.playlistweb.restcontroller.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jypark.playlistweb.service.youtube.YouTubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainRestController {

    private final YouTubeService youTubeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MainRestController(YouTubeService youTubeService, ObjectMapper objectMapper) {
        this.youTubeService = youTubeService;
        this.objectMapper = objectMapper;
    }

    /**
     * 선택된 재생목록의 곡 리스트를 반환하는 API
     * @param id 재생목록 ID
     * @param authorizedClient Google OAuth2 인증된 클라이언트
     * @return 곡의 제목, 아티스트, 앨범 커버 URL 정보를 포함한 리스트
     */
    @GetMapping("/playlist/{id}")
    public List<Map<String, String>> getPlaylistSongs(@PathVariable String id, @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        // 반환할 곡 리스트를 저장할 리스트
        List<Map<String, String>> songs = new ArrayList<>();

        try {
            // YouTubeService를 호출하여 재생목록 항목 JSON 데이터를 가져옴
            String playlistItemsJson = youTubeService.getPlaylistItems(id, authorizedClient).block();

            if (playlistItemsJson != null) {
                JsonNode items = objectMapper.readTree(playlistItemsJson).get("items");
                if (items != null) {
                    for (JsonNode item : items) {
                        // 곡 제목 추출
                        String title = item.get("snippet").get("title").asText();
                        // 아티스트 추출
                        String artist = item.get("snippet").get("videoOwnerChannelTitle").asText();
                        // 앨범 커버 URL 추출
                        String albumCover = item.get("snippet").get("thumbnails").get("default").get("url").asText();

                        // 곡 데이터를 Map으로 저장하고 리스트에 추가
                        songs.add(Map.of("title", title, "artist", artist, "albumCover", albumCover));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while fetching playlist items", e);
        }

        return songs;
    }
}

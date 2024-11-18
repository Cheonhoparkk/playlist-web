package com.jypark.playlistweb.restcontroller.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jypark.playlistweb.service.youtube.YouTubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     */
    @GetMapping("/playlist/{id}")
    public List<Map<String, String>> getPlaylistSongs(@PathVariable String id, @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        List<Map<String, String>> songs = new ArrayList<>();

        try {
            String playlistItemsJson = youTubeService.getPlaylistItems(id, authorizedClient).block();

            if (playlistItemsJson != null) {
                JsonNode items = objectMapper.readTree(playlistItemsJson).get("items");
                if (items != null) {
                    for (JsonNode item : items) {
                        String title = item.get("snippet").get("title").asText();
                        String artist = item.get("snippet").get("videoOwnerChannelTitle").asText();
                        String albumCover = item.get("snippet").get("thumbnails").get("default").get("url").asText();
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

package com.jypark.playlistweb.service.youtube;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class YouTubeService {

    private final WebClient webClient;

    // 생성자 주입을 통해 webClient와 API 기본 URL 설정
    public YouTubeService(WebClient.Builder webClientBuilder,
                          @Value("${google.api.url}") String googleApiUrl) {
        // webClient에 기본 URL 설정 (application-google.yml에서 값 가져옴)
        this.webClient = webClientBuilder
                .baseUrl(googleApiUrl)
                .build();
    }

    /**
     * 사용자의 YouTube 재생목록을 가져오는 메서드
     * @param authorizedClient OAuth2 인증된 클라이언트 (Google 계정으로 인증됨)
     * @return 사용자의 YouTube 재생목록 데이터를 Mono로 반환 (비동기 처리)
     */
    public Mono<String> getPlaylists(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        return webClient
                .get() // HTTP GET 요청 설정
                .uri("/youtube/v3/playlists?part=snippet&mine=true") // API 엔드포인트 URI (사용자의 재생목록 요청)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue())) // 인증 토큰을 헤더에 추가
                .retrieve() // 서버 응답을 받아 처리 시작
                .bodyToMono(String.class);  // JSON 응답을 String으로 반환
    }

    public Mono<String> getPlaylistItems(String playlistId, OAuth2AuthorizedClient authorizedClient) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/playlistItems")
                        .queryParam("part", "snippet")
                        .queryParam("mine", "true")
                        .queryParam("playlistId", playlistId)
                        .queryParam("maxResults", 50) // 원하는 결과 수
                        .build())
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue())) // OAuth2 토큰 설정
                .retrieve()
                .bodyToMono(String.class); // JSON 응답을 String으로 반환
    }
}

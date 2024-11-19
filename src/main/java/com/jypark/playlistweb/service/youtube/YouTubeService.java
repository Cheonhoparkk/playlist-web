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
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/playlists") // YouTube Data API에서 사용자의 재생목록을 가져오기 위한 엔드포인트
                        .queryParam("part", "snippet") // 요청할 리소스의 세부정보를 지정. "snippet"은 제목, 설명 등의 정보를 포함
                        .queryParam("mine", "true") // 사용자의 재생목록만 가져오도록 설정. true는 로그인된 사용자와 연결된 재생목록을 의미
                        .queryParam("maxResults", 50) // 최대 50개의 재생목록 요청
                        .build())
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue())) // 인증 토큰을 헤더에 추가
                .retrieve() // 서버 응답을 받아 처리 시작
                .bodyToMono(String.class);  // JSON 응답을 String으로 반환
    }

    /**
     * 사용자의 YouTube 재생목록의 곡들을 가져오는 메서드
     * @param authorizedClient OAuth2 인증된 클라이언트 (Google 계정으로 인증됨)
     * @return 사용자의 YouTube 재생목록 곡 데이터를 Mono로 반환 (비동기 처리)
     */
    public Mono<String> getPlaylistItems(String playlistId, OAuth2AuthorizedClient authorizedClient) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/playlistItems") // YouTube Data API에서 특정 재생목록의 곡들을 가져오기 위한 엔드포인트 설정
                        .queryParam("part", "snippet") // 요청할 리소스의 세부정보를 지정. "snippet"은 곡 제목, 설명, 썸네일 등의 정보를 포함
                        .queryParam("mine", "true") // 사용자의 데이터만 가져오도록 설정. true는 인증된 사용자의 데이터에 접근
                        .queryParam("playlistId", playlistId) // 재생목록 ID를 지정. 이 ID는 어떤 재생목록의 데이터를 가져올지를 명시
                        .queryParam("maxResults", 50) // 원하는 결과 수
                        .build())
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue())) // OAuth2 토큰 설정
                .retrieve()
                .bodyToMono(String.class); // JSON 응답을 String으로 반환
    }
}

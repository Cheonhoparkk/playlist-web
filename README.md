# 유튜브 api를 이용한 뮤직 플레이리스트 웹 (Music playlist web using YouTube API)

## 가이드 (Guide)

###  application-database.yml 및 application-google.yml 직접 만들어서 넣어야함
[application-google.yml]
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your_id
            client-secret: your_secret
            scope: profile, email, https://www.googleapis.com/auth/youtube.readonly
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://www.googleapis.com/oauth2/v3/userinfo
            
[application-google.yml]


## 개발 일지

### 2024-11-06: 프로젝트 초기 세팅 및 GitHub 연동

### 2024-11-14: index 페이지로 이동시 로그인 페이지로 이동하게 수정

### 2024-11-15: Spring Security를 사용해 Google 계정 로그인 기능 구현 및 index페이지로 이동시 로그인 여부에 따라 로그인 페이지 아니면 메인 페이지로 이동하게 security 설정

### 2024-11-18: 유튜브 api 연동 성공 및 플레이리스트 클릭시 => 곡 리스트 표출

### 2024-11-19: 곡 리스트 클릭 시, 해당 재생목록(유튜브 뮤직) 페이지로 이동

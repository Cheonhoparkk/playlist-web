jQuery(document).ready(function () {
    let allSongs = [];  // 곡 데이터를 저장할 배열
    let currentPlaylistId = ''; // 현재 재생목록 ID를 저장

    // "로고" 클릭 이벤트 처리
    jQuery('#logo-link').on('click', function () {
        location.reload(); // 페이지 새로고침
    });

    // "홈" 링크 클릭 이벤트 처리
    jQuery('#home-link').on('click', function (event) {
        event.preventDefault(); // 링크 기본 동작 방지
        jQuery('#song-cards').empty(); // 곡 리스트 영역 비우기
    });

    // 재생목록 항목 클릭 이벤트 처리
    jQuery('.playlist-item').on('click', function (event) {
        event.preventDefault(); // 링크 기본 동작 방지
        currentPlaylistId  = jQuery(this).data('id'); // 클릭된 재생목록의 ID 가져오기
        const playlistName = jQuery(this).text(); // 클릭된 재생목록의 이름 가져오기

        // 클릭한 재생목록 이름으로 변경
        jQuery('.content h2').text(playlistName);

        // AJAX 요청
        jQuery.ajax({
            url: `/api/playlist/${currentPlaylistId}`,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                // 기존 곡 리스트 제거
                jQuery('#song-cards').empty();

                // 새 곡 리스트 추가
                data.forEach(function (song, index) {
                    const card = `
                        <div class="card" 
                             data-video-id="${song.videoId}" 
                             data-playlist-id="${currentPlaylistId}" 
                             data-index="${index + 1}"> <!-- YouTube에서 index는 1부터 시작 -->
                            <img src="${song.albumCover}" alt="앨범 커버" style="width: 100px; height: 100px;">
                            <p>${song.title}</p>
                            <p>${song.artist}</p>
                        </div>
                    `;
                    jQuery('#song-cards').append(card);
                });

                allSongs = data; // 선택한 재생목록 데이터 저장
            },
            error: function (error) {
                console.error('Error fetching playlist songs:', error);
            }
        });
    });

    // 검색 입력 필드 이벤트 처리
    jQuery('#search-input').on('input', function() {
       const searchQuery = jQuery(this).val().toLowerCase(); // 입력된 검색어
       const filteredSongs = allSongs.filter(song =>
        song.title.toLowerCase().includes(searchQuery) ||
        song.artist.toLowerCase().includes(searchQuery)
       );
       renderSongs(filteredSongs, currentPlaylistId);  // 필터링된 곡 리스트 렌더링
    });

    // 곡 리스트를 렌더링하는 함수
    function renderSongs(songs, playlistId) {
        jQuery('#song-cards').empty();   // 기존 곡 리스트 제거

        if(songs.length === 0) {
            jQuery('#song-cards').append('<p>검색 결과가 없습니다.</p>');
        } else {
            songs.forEach(function(song, index) {
                const card = `
                    <div class="card" 
                         data-video-id="${song.videoId}" 
                         data-playlist-id="${playlistId}" 
                         data-index="${index + 1}">
                        <img src="${song.albumCover}" alt="앨범 커버" style="width: 100px; height: 100px;">
                        <p>${song.title}</p>
                        <p>${song.artist}</p>
                    </div>
                `;
                jQuery('#song-cards').append(card);
            });
        }
    }

    // 곡 클릭 이벤트 처리
    jQuery('#song-cards').on('click', '.card', function() {
        const videoId = jQuery(this).data('video-id'); // 클릭된 카드에서 동영상 ID 가져오기
        const playlistId = jQuery(this).data('playlist-id'); // 선택한 곡이 속한 재생목록 ID
        const index = jQuery(this).data('index'); // 선택한 곡의 인덱스

        if(videoId && playlistId && index) {
            console.log('Video ID:', videoId, 'Playlist ID:', playlistId, 'Index:', index);
            const playlistUrl = `https://music.youtube.com/watch?v=${videoId}&list=${playlistId}&index=${index}`;
            window.open(playlistUrl, '_blank'); // 새 탭에서 재생목록 열기
        } else {
            console.error('곡 정보를 가져올 수 없습니다:', { videoId, playlistId, index });
            alert('곡 정보를 불러올 수 없습니다.');
        }
    });


});

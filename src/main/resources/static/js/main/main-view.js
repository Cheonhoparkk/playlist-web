$(document).ready(function () {
    // "홈" 링크 클릭 이벤트 처리
    $('#home-link').on('click', function (event) {
        event.preventDefault(); // 링크 기본 동작 방지
        $('#song-cards').empty(); // 곡 리스트 영역 비우기
    });

    // 재생목록 항목 클릭 이벤트 처리
    $('.playlist-item').on('click', function (event) {
        event.preventDefault(); // 링크 기본 동작 방지
        const playlistId = $(this).data('id'); // 클릭된 재생목록의 ID 가져오기

        // AJAX 요청
        $.ajax({
            url: `/api/playlist/${playlistId}`,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                // 기존 곡 리스트 제거
                $('#song-cards').empty();

                // 새 곡 리스트 추가
                data.forEach(function (song) {
                    const card = `
                        <div class="card">
                            <img src="${song.albumCover}" alt="앨범 커버" style="width: 100px; height: 100px;">
                            <p>${song.title}</p>
                            <p>${song.artist}</p>
                        </div>
                    `;
                    $('#song-cards').append(card);
                });
            },
            error: function (error) {
                console.error('Error fetching playlist songs:', error);
            }
        });
    });
});

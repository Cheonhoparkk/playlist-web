$(document).ready(function () {
    const container = $("#playlist-container");

    // Thymeleaf에서 전달된 데이터를 사용하는 전역 변수
    const playlists = playlistsData || '[]'; // HTML에서 선언된 playlistsData 사용
    console.log("Playlists raw data from server:", playlists);

    try {
        const playlistData = JSON.parse(playlists);
        console.log("Parsed playlist data:", playlistData);

        if (playlistData && playlistData.items && playlistData.items.length > 0) {
            playlistData.items.forEach(playlist => {
                const playlistElement = `
                    <a href="/playlist/${playlist.id}" class="playlist-link">
                        <div class="playlist">
                            <img src="${playlist.snippet.thumbnails.medium.url}" alt="Playlist Thumbnail" class="thumbnail">
                            <h3>${playlist.snippet.title}</h3>
                            <p>${playlist.snippet.description || "No description available"}</p>
                        </div>
                    </a>
                `;
                container.append(playlistElement);
            });
        } else {
            container.html("<p>No playlists available</p>");
        }
    } catch (error) {
        console.error("Error parsing playlist data:", error);
        container.html("<p>Error loading playlists</p>");
    }
});

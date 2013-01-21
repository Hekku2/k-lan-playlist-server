<link rel="stylesheet" type="text/css" href="/css/NowPlayingBar.css">
<div class="now-playing-bar">
	<#if getNowPlaying??>
		<p class="now-playing-bar-song-name"> Currently playing: ${getNowPlaying} </p>
	<#else>
		<p class="now-playing-bar-song-name"> Nothing playing. </p>
	</#if>
	
</div>
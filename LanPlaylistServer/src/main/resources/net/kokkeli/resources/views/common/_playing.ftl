<link rel="stylesheet" type="text/css" href="/resource/css/NowPlayingBar.css">
<script type="text/javascript" src="/resource/js/play-controls.js"></script>

<div class="now-playing-bar">
	<button id="play" class="btn pull-right">Play</button>

	<#if getNowPlaying??>
		<p class="now-playing-bar-song-name"> Currently playing: ${getNowPlaying} </p>
	<#else>
		<p class="now-playing-bar-song-name"> Nothing playing. </p>
	</#if>
</div>
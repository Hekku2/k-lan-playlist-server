<link rel="stylesheet" type="text/css" href="/css/NowPlayingBar.css">
<div class="now-playing-bar">
	<#if getNowPlaying??>
		Currently playing: ${getNowPlaying}
	<#else>
		Nothing playing.
	</#if>
	
</div>
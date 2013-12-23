<link rel="stylesheet" type="text/css" href="/resource/css/NowPlayingBar.css">
<script type="text/javascript" src="/resource/js/play-controls.js"></script>

<div class="now-playing-bar">
	<button id="stop" class="btn" <#if !getAnythingPlaying>disabled</#if>><i class="icon-stop"></i></button>
	<button id="pause" class="btn" <#if !getAnythingPlaying>disabled</#if>><i class="icon-pause"></i></button>
	<button id="play" class="btn" <#if !getAnythingPlaying>disabled</#if>><i class="icon-play"></i></button>
	<span class="now-playing-bar-song-name">
		<#if getNowPlaying??> <strong>${getNowPlaying}</strong> <#else>Nothing playing. </#if>
	</span>
</div>
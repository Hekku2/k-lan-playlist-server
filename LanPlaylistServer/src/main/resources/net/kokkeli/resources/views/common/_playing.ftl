<link rel="stylesheet" type="text/css" href="/resource/css/NowPlayingBar.css">
<script type="text/javascript" src="/resource/js/play-controls.js"></script>

<div class="now-playing-bar">
	<#if getUserRole == 3>
		<!-- Not yet implemented.
		<button id="stop" class="btn" <#if !getAnythingPlaying>disabled</#if>><i class="icon-stop"></i></button>
		-->
		<button id="pause" type="button" class="btn btn-default" <#if !getAnythingPlaying>disabled</#if>><span class="glyphicon glyphicon-pause"></span></button>
		<button id="play" type="button" class="btn btn-default" <#if !getAnythingPlaying>disabled</#if>><span class="glyphicon glyphicon-play"></span></button>
	</#if>
	<span class="now-playing-bar-song-name">
		<strong id="now-playing">Fetching...</strong>
	</span>
</div>
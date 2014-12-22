<#assign header = "Playlist details">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<#include "/common/_playlist.ftl">
			<div>
				<a class="btn btn-default" href="/playlists">Back</a>
			</div
		</div>
	</body>
</html> 
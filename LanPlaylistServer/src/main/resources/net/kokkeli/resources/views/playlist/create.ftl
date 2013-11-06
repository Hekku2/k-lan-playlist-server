<#assign header = "Create playlist">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/Playlist.css">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form method="POST" class="value-fields">
					<div class="field">
						<div class="description">
							Playlist name: 
						</div>
						<div class=value>
							<input type="text" name="name"">
						</div>
					</div>
					<div class="navi">
						<input class="button" type="submit" value="Create">
						<a class="button" href="/playlists">Back</a>
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
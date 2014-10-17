<#assign header = "Create playlist">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<form method="POST" class="form-horizontal">
				<@valueField label="Playlist name" name="name" />
				<div>
					<input class="btn btn-primary" type="submit" value="Create">
					<a class="btn btn-default" href="/playlists">Back</a>
				</div>
			</form>
		</div>
	</body>
</html> 
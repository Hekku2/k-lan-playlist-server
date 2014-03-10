<#assign header = "Add to playlist">
<#assign tab = 2>
<#assign selectedMethod = 1>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/PlaylistAdd.css">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<#include "/playlist/_method.ftl">
				<p>Url can be any location that vlc can handle.</p>
				<form action="" method="post" class="form-horizontal">
					<input type="hidden" name="playlistid" value="${getModel.getPlaylistId}">
					<fieldset>
						<legend>Track data:</legend>
						<@valueField label="Artist" name="artist" />
						<@valueField label="Track" name="track" />
						<@valueField label="Url" name="url" />
						<div class="controls">
							<input type="submit" value="Send" class="btn btn-primary">
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</body>
</html> 
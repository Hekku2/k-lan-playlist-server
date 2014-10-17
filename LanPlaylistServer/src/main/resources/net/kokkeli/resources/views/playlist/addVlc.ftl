<#assign header = "Add to playlist">
<#assign tab = 2>
<#assign selectedMethod = 1>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/PlaylistAdd.css">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<#include "/playlist/_method.ftl">
			
			<form action="" method="post" class="form-horizontal">
				<input type="hidden" name="playlistid" value="${getModel.getPlaylistId}">
				<fieldset>
					<legend>Track data:</legend>
					<@valueField label="Artist" name="artist" />
					<@valueField label="Track" name="track" />
					<div class="form-group">
						<label for="url" class="col-md-2 control-label">Url</label>
						<div class="col-md-4">
							<input type="text" class="form-control" name="url" value="">
						</div>
						<span class="help-block">Url can be any location that vlc can handle.</span>
					</div>
					<div class="controls">
						<input type="submit" value="Send" class="btn btn-primary">
					</div>
				</fieldset>
			</form>
		</div>
	</body>
</html> 
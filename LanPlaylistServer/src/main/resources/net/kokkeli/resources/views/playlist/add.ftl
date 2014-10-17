<#assign header = "Add to playlist">
<#assign tab = 2>
<#assign selectedMethod = 0>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/PlaylistAdd.css">
	<script type="text/javascript" src="/resource/js/playlist-add.js"></script>
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<#include "/playlist/_method.ftl">
			<form id="form" action="" enctype="multipart/form-data" method="post" class="form-horizontal">
				<fieldset>
					<legend>Track data:</legend>
					<@valueField label="Artist" name="artist" />
					<@valueField label="Track" name="track" />
					<div class="form-group">
					    <label class="control-label col-md-2" for="file">File</label>
					    <div class="col-md-4">
					    	<input class="form-control" type="file" id="file" name="file" size="40">
					    </div>
					</div>
					<div class="controls">
						<div id="validation-error"></div>
						<input type="submit" value="Send" class="btn btn-primary">
					</div>
				</fieldset>
			</form>
		</div>
	</body>
</html> 
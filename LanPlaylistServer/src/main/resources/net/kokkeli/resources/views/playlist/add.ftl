<#assign header = "Add to playlist">
<#assign tab = 2>
<#assign selectedMethod = 0>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/jquery-fileupload-ui/jquery.fileupload.css">
	<link rel="stylesheet" type="text/css" href="/resource/css/PlaylistAdd.css">
	<script type="text/javascript" src="/resource/js/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/resource/js/jquery-file-upload/jquery.fileupload.js"></script>
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
						<label class="col-md-2 control-label">File upload</label>
						<div class="col-md-4">
							<span class="btn btn-success fileinput-button">
						        <i class="glyphicon glyphicon-plus"></i>
						        <span>Select file</span>
						        <input type="file" id="file" name="file" size="40">
							</span>
							<span id="filename" class="help-block"></span>
						</div>
					</div>
					<div class="controls">
						<input type="submit" value="Send" class="btn btn-primary" data-loading-text="Loading...">
					</div>
				</fieldset>
			</form>
		</div>
	</body>
</html> 
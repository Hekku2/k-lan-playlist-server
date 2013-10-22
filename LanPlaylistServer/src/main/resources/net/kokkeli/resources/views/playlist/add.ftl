<#assign header = "Add to playlist">
<#assign tab = 2>
<#assign selectedMethod = 0>

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
				<form action="" enctype="multipart/form-data" method="post" class="form-horizontal">
					<fieldset>
						<legend>Track data:</legend>
						<@valueField label="Artist" name="artist" />
						<@valueField label="Track" name="track" />
						<div class="controls">
							Please specify a file, or a set of files:<br>
							<input type="file" name="file" size="40">
						</div>
						<div class="controls">
							<input type="submit" value="Send" class="btn btn-primary">
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</body>
</html> 
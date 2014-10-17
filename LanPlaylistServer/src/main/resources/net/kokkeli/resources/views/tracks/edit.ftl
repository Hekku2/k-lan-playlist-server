<#assign header = "Track edit">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<form class="form-horizontal" method="post">
				<@valueField label="Track" name="track" value=getModel.getTrack />
				<@valueField label="Artist" name="artist" value=getModel.getArtist />
				<@valueField label="Location" name="location" value=getModel.getLocation />
				<@uneditableValueField label="Uploader" value=getModel.getUploader />
				<div class="controls">
					<input type="submit" value="Save" class="btn btn-primary">
					<a class="btn btn-default" href="/tracks/${getModel.getId}">Back</a>
				</div>
			</form>
		</div>
	</body>
</html> 
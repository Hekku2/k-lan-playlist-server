<#assign header = "Track details">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form class="form-horizontal" method="post">
					<@valueField label="Track" name="track" value=getModel.getTrackName />
					<@valueField label="Artist" name="artist" value=getModel.getArtist />
					<@valueField label="Location" name="location" value=getModel.getLocation />
					<@uneditableValueField label="Uploader" value=getModel.getUploader />
					<div class="controls">
						<a class="btn" href="/tracks/${getModel.getId}">Back</a>
						<input type="submit" value="Save" class="btn btn-primary">
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
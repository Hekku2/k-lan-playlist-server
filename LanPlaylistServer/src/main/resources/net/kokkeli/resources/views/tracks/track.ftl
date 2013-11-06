<#assign header = "Track details">
<#assign tab = 1>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form class="form-horizontal">
					<div class="form-horizontal">
						<@uneditableValueField label="Track" value=getModel.getTrackName />
						<@uneditableValueField label="Artist" value=getModel.getArtist />
						<@uneditableValueField label="Location" value=getModel.getLocation />
						<@uneditableValueField label="Uploader" value=getModel.getUploader />
					</div>
					<div class="controls">
						<a class="btn" href="/tracks">Back</a>
						<a class="btn" href="/tracks/edit/${getModel.getId}">Edit</a>
					</div>
				<form>
			</div>
		</div>
	</body>
</html> 
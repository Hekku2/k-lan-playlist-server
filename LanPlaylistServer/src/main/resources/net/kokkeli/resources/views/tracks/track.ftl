<#assign header = "Track details">
<#assign tab = 1>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<div class="form-horizontal">
					<@uneditableValueField label="Track" value=getModel.getTrackName />
					<@uneditableValueField label="Artist" value=getModel.getArtist />
					<@uneditableValueField label="Uploader" value=getModel.getUploader />
					<@uneditableValueField label="Location" value=getModel.getLocation />
				</div>
				<div class="navi">
					<a class="button" href="/tracks/edit/${getModel.getId}">Edit</a>
					<a class="button" href="/tracks">Back</a>
				</div>
			</div>
		</div>
	</body>
</html> 
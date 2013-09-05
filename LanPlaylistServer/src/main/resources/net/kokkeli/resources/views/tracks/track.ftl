<#assign header = "User details">
<#assign tab = 1>

<#macro valueField label value>
	<div class="control-group">
		<div class="control-label">${label}</div>
		<div class="controls">
			<span class="input-xlarge uneditable-input">${value}</span>
		</div>
	</div>
</#macro>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<div class="form-horizontal">
					<@valueField label="Track" value=getModel.getTrackName />
					<@valueField label="Artist" value=getModel.getArtist />
					<@valueField label="Uploader" value=getModel.getUploader />
					<@valueField label="Location" value=getModel.getLocation />
				</div>
				<div class="navi">
					<a class="button" href="/tracks/edit/${getModel.getId}">Edit</a>
					<a class="button" href="/tracks">Back</a>
				</div>
			</div>
		</div>
	</body>
</html> 
<#assign header = "Main page">
<#assign tab = 0>
<!DOCTYPE HTML>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="container">
			<#include "common/_topsection.ftl">
			<#include "common/_playing.ftl">
			<h1>${header}</h1>
			<#include "common/_info_error.ftl">
			<#if getModel?? && getModel.getItems??>
				<a class="btn btn-default" href="/playlists/add/upload/${getModel.getId}">Add song</a>
				<#include "common/_playlist.ftl">
			<#else>
				No playlist loaded.
			</#if>
		</div>
	</body>
</html> 
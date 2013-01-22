<#assign header = "Main page">
<#assign tab = 0>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<#include "common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#if getModel?? && getModel.getItems??>
					<a class="button" href="/playlists/add/${getModel.getId}">Add song</a>
					<#include "common/_playlist.ftl">
				<#else>
					No playlist loaded.
				</#if>
			</div>
		</div>
	</body>
</html> 
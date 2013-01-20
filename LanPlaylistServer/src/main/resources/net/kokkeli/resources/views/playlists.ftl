<#assign header = "Playlists">
<#assign tab = 2>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<#include "common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#if getModel?? && getModel.getItems??>
					<#include "_playlists.ftl">
				<#else>
					No playlists found.
				</#if>
			</div>
		</div>
	</body>
</html> 
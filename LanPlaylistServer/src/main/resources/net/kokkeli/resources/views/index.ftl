<#assign header = "Main page">

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#if playlist??>
					<#include "_playlist.ftl">
				<#else>
					<p> No playlist loaded </p>
				</#if>
			</div>
		</div>
	</body>
</html> 
<#assign header = "Management">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<a class="btn btn-default" href="/tracks">Verify track database</a>
				<a class="btn btn-default" href="/fetchers">View fetch requests</a>
				<a class="btn btn-default" href="/log">View log</a>
			</div>
		</div>
	</body>
</html> 
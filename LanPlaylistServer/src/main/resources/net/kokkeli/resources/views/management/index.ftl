<#assign header = "Management">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<a class="btn" href="/tracks">Verify track database</a>
				<a class="btn" href="/fetchers">View fetch requests</a>
				<a class="btn" href="/log">View log</a>
			</div>
		</div>
	</body>
</html> 
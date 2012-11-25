<#assign header = "Users">

<html>
	<#include "common/_header.ftl">
	<body>
		<#include "common/_topsection.ftl">
		<h1>${header}</h1>
		<div class="content">
			<td>${getUsername}</td>
			<td>${getRole}</td>
		</div>
	</body>
</html> 
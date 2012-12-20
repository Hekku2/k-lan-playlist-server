<#assign header = "Users">

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<td>${getUsername}</td>
				<td>${getRole}</td>
			</div>
		</div>
	</body>
</html> 
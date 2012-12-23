<#assign header = "Users">

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<a href="/users">Back</a>
				<td>${getUsername}</td>
				<td>${getRole}</td>
				<td><a href="/users/edit/${getId}">Edit</a></td>
			</div>
		</div>
	</body>
</html> 
<#assign header = "Users">

<html>
	<#include "common/_header.ftl">
	<body>
		<#include "common/_topsection.ftl">
		<h1>${header}</h1>
		<div class="content">
			<#if getUsers??>
				<table>
					<tr>
						<th>Username</th>
						<th>Role</th>
						<th>Man.<th>
					</tr>
					<#list getUsers as item>
						<tr>
							<td>${item.getUsername}</td>
							<td>${item.getRole}</td>
							<td>Del./Mod.</td>
						</tr>
					</#list>
				</table>
			<#else>
				<p> No users. </p>
			</#if>
		</div>
	</body>
</html> 
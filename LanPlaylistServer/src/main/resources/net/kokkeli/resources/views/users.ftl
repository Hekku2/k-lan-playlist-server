<#assign header = "Users">
<#assign tab = 1>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<a class="button" href="users/create">Create</a>
				<#if getModel.getUsers??>
					<table>
						<tr>
							<th>Username</th>
							<th>Role</th>
							<th>Man.<th>
						</tr>
						<#list getModel.getUsers as item>
							<tr>
								<td>${item.getUsername}</td>
								<td>${item.getRole}</td>
								<td><a href="/users/${item.getId}">Details</a></td>
							</tr>
						</#list>
					</table>
				<#else>
					<p> No users. </p>
				</#if>
			</div>
		</div>
	</body>
</html> 
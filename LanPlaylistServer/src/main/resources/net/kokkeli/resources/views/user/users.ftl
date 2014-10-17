<#assign header = "Users">
<#assign tab = 1>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			
			<#include "/common/_info_error.ftl">
			<a class="btn btn-default" href="users/create">Create</a>
			<#if getModel.getUsers??>
				<table class="table table-striped">
					<tr>
						<th>Username</th>
						<th>Role</th>
						<th></th>
					</tr>
					<#list getModel.getUsers as item>
						<tr>
							<td>${item.getUsername}</td>
							<td>${item.getRole}</td>
							<td><a title="Edit" class="btn btn-default" href="/users/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a></td>
						</tr>
					</#list>
				</table>
			<#else>
				<p> No users. </p>
			</#if>
			
		</div>
	</body>
</html> 
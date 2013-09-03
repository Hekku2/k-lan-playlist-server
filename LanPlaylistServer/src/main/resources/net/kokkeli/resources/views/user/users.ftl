<#assign header = "Users">
<#assign tab = 1>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<a class="button" href="users/create">Create</a>
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
								<td><a title="Edit" class="btn" href="/users/${item.getId}"><i class="icon-edit"></i></a></td>
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
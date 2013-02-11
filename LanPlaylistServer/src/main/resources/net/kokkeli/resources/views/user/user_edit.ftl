<#assign header = "Edit User">
<#assign tab = 1>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form method="POST" class="value-fields">
					<input type="hidden" name="id" value="${getModel.getId}">
					<div class="field">
						<div class="description">
							Username: 
						</div>
						<div class=value>
							<input type="text" name="username" value="${getModel.getUsername}">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Role:
						</div>
						<div class="value">
							User <input type="radio" name="role" value="user"  <#if getModel.getRole == "USER">checked</#if>>
							Admin <input type="radio" name="role" value="admin" <#if getModel.getRole == "ADMIN">checked</#if>>
						</div>
					</div>
					<input class="button" type="submit" value="Edit">
					<a class="button" href="/users/${getModel.getId}">Cancel</a>
				</form>
			</div>
		</div>
	</body>
</html> 
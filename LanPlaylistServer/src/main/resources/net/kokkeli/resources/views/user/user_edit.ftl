<#assign header = "Edit User">
<#assign tab = 1>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form method="POST" class="form-horizontal">
					<input type="hidden" name="id" value="${getModel.getId}">
					<@valueField label="Username" name="username" value=getModel.getUsername />
					<div class="control-group">
						<div class="control-label">Role</div>
						<div class="controls">
							<label class="radio">
							  <input type="radio" name="role" value="1" <#if getModel.getRole == "USER">checked</#if>>
							  User
							</label>
							<label class="radio">
							  <input type="radio" name="role" value="2" <#if getModel.getRole == "ADMIN">checked</#if>>
							  Admin
							</label>
						</div>
					</div>
					<@passwordField label="Password" name="newPassword" />
					<@passwordField label="Confirm password" name="confirmPassword" />
					<input class="button" type="submit" value="Edit">
					<a class="button" href="/users/${getModel.getId}">Cancel</a>
				</form>
			</div>
		</div>
	</body>
</html> 
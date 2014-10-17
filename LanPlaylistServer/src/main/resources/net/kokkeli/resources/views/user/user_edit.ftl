<#assign header = "Edit User">
<#assign tab = 1>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<form method="POST" class="form-horizontal">
				<input type="hidden" name="id" value="${getModel.getId}">
				<@valueField label="Username" name="username" value=getModel.getUsername />
				
				<div class="form-group">
					<label class="col-md-2 control-label">Role:</label>
					<div class="col-md-10">
						<div class="radio">
						  <label>
						    <input type="radio" name="role" id="optionsRadios1" value="1" <#if getModel.getRole == "USER">checked</#if>>
						    User
						  </label>
						</div>
						<div class="radio">
						  <label>
						    <input type="radio" name="role" id="optionsRadios1" value="2" <#if getModel.getRole == "ADMIN">checked</#if>>
						    Admin
						  </label>
						</div>
					</div>
				</div>
				<@passwordField label="Password" name="newpassword" />
				<@passwordField label="Confirm password" name="confirmpassword" />
				<input class="btn" type="submit" value="Edit">
				<a class="btn" href="/users/${getModel.getId}">Cancel</a>
			</form>
		</div>
	</body>
</html> 
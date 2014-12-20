<#assign header = "Create User">
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
				<@valueField label="Username" name="username" />
				<div class="form-group">
					<label class="col-md-2 control-label">Role:</label>
					<div class="col-md-10">
						<div class="radio">
						  <label>
						    <input type="radio" name="role" value="2">
						    User
						  </label>
						</div>
						<div class="radio">
						  <label>
						    <input type="radio" name="role" value="3">
						    Admin
						  </label>
						</div>
					</div>
				</div>
				<@passwordField label="Password" name="newpassword" />
				<@passwordField label="Confirm password" name="confirmpassword" />
				<div>
					<input class="btn btn-primary" type="submit" value="Create">
					<a class="btn" href="/users">Cancel</a>
				</div>
			</form>
		</div>
	</body>
</html> 
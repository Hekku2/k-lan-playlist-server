<#assign header = "Create User">
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
					<@valueField label="Username" name="username" />
					<div class="control-group">
						<div class="control-label">Role</div>
						<div class="controls">
							<label class="radio">
							  <input type="radio" name="role" value="1">
							  User
							</label>
							<label class="radio">
							  <input type="radio" name="role" value="2">
							  Admin
							</label>
						</div>
					</div>
					<@passwordField label="Password" name="newpassword" />
					<@passwordField label="Confirm password" name="confirmpassword" />
					<div class="submit-box">
						<input class="btn" type="submit" value="Create">
						<a class="btn" href="/users">Cancel</a>
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
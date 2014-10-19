<#assign header = "User details">
<#assign tab = 1>
<!DOCTYPE HTML>

<html lang="en">
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<form class="form-horizontal" role="form">
				<div class="form-group">
					<label class="col-md-2 control-label">Username:</label>
					<p class="col-md-10 form-control-static" id="username">${getModel.getUsername}</p>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">Role:</label>
					<div class="col-md-10">
						<p class="form-control-static" id="role">${getModel.getRole}</p>
					</div>
				</div>
			</form>
			<div class="navi">
				<a class="btn btn-default" href="/users/edit/${getModel.getId}">Edit</a>
				<a class="btn btn-default" href="/users">Back</a>
			</div>
		</div>
	</body>
</html> 
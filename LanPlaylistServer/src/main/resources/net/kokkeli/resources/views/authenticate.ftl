<#assign header = "Sign in">
<!DOCTYPE HTML>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="container">
			<h1>${header}</h1>
			<#include "common/_info_error.ftl">
			<form class="form-horizontal" role="form" action="authentication" method="post">
				<div class="form-group form-group-lg">
					<label for="user" class="col-sm-1 control-label">Username</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="user" placeholder="Username" name="user">
					</div>
				</div>
				<div class="form-group form-group-lg">
					<label for="pwd" class="col-sm-1 control-label">Password</label>
					<div class="col-sm-3">
						<input type="password" class="form-control" id="pwd" placeholder="Password" name="pwd">
					</div>
				</div>
				<div class="form-group form-group-lg">
					<div class="col-sm-offset-3">
						<button type="submit" class="btn btn-primary">Sign in</button>
					</div>
				</div>
			</form>
		</div>
	</body>
</html> 
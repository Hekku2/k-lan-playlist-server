<#assign header = "Sign in">
<!DOCTYPE HTML>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="container">
			<h1>${header}</h1>
			<#include "common/_info_error.ftl">
			<form class="form-horizontal col-md-6" role="form" action="authentication" method="post">
				<div class="form-group form-group-lg">
					<label for="user" class="col-md-3 control-label">Username</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="user" placeholder="Username" name="user">
					</div>
				</div>
				<div class="form-group form-group-lg">
					<label for="pwd" class="col-md-3 control-label">Password</label>
					<div class="col-md-9">
						<input type="password" class="form-control" id="pwd" placeholder="Password" name="pwd">
					</div>
				</div>
				<div class="form-group form-group-lg">
					<div class="col-md-offset-7 col-md-2">
						<button type="submit" class="btn btn-primary">Sign in</button>
					</div>
					<#if isAuthenticationRequired == false>
						<div class="col-md-3">
							<a href="/index" class="btn btn-default">Don't  sign in</a>
						</div>
					</#if>
				</div>
			</form>
		</div>
	</body>
</html> 
<#assign header = "Authentication">
<!DOCTYPE HTML>

<link rel="stylesheet" type="text/css" href="/resource/css/Authentication.css">

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "common/_info_error.ftl">
				<form name="input" action="authentication" method="post" class="authentication-box">
					<div class="field">
						<div class="description">
							Username: 
						</div>
						<div class=value>
							<input type="text" name="user">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Password:
						</div>
						<div class=value>
							<input type="password" name="pwd">
						</div>
					</div>
					<div class="login-button-container">
						<input type="submit" value="Log in" class="button">
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
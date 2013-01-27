<#assign header = "Create User">
<#assign tab = 1>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "common/_info_error.ftl">
				<form method="POST" class="value-fields">
					<div class="field">
						<div class="description">
							Username: 
						</div>
						<div class=value>
							<input type="text" name="username">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Role:
						</div>
						<div class="value">
							User <input type="radio" name="role" value="user" checked>
							Admin <input type="radio" name="role" value="admin">
						</div>
					</div>
					<div class="submit-box">
						<input class="button" type="submit" value="Create">
						<a class="button" href="/users">Cancel</a>
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
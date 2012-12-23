<#assign header = "Users">

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<form method="POST" class="edit-form">
					<input type="hidden" name="id" value="${getId}">
					<div class="field">
						<div class="description">
							Username: 
						</div>
						<div class=value>
							<input type="text" name="username" value="${getUsername}">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Role:
						</div>
						<div class="value">
							User <input type="radio" name="role" value="user"  <#if getRole == "USER">checked</#if>>
							Admin <input type="radio" name="role" value="admin" <#if getRole == "ADMIN">checked</#if>>
						</div>
					</div>
					<input type="submit" value="Edit">
					<a href="/users/${getId}">Cancel</a>
				</form>
			</div>
		</div>
	</body>
</html> 
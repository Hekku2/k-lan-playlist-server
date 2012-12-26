<#assign header = "Users">

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<div class="field">
					<div class="description">
						Username: 
					</div>
					<div class=value>
						${getUsername}
					</div>
				</div>
				<div class="field">
					<div class="description">
						Role:
					</div>
					<div class="value">
						${getRole}
					</div>
				</div>
				<div class="navi">
					<a href="/users/edit/${getId}">Edit</a>
					<a href="/users">Back</a>
				</div>
			</div>
		</div>
	</body>
</html> 
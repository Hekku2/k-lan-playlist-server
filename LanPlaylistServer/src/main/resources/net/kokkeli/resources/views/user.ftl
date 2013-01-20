<#assign header = "User details">
<#assign tab = 1>

<html>
	<#include "common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<div class="value-fields">
					<div class="field">
						<div class="description">
							Username: 
						</div>
						<div class=value>
							${getModel.getUsername}
						</div>
					</div>
					<div class="field">
						<div class="description">
							Role:
						</div>
						<div class="value">
							${getModel.getRole}
						</div>
					</div>
				</div>
				<div class="navi">
					<a class="button" href="/users/edit/${getModel.getId}">Edit</a>
					<a class="button" href="/users">Back</a>
				</div>
			</div>
		</div>
	</body>
</html> 
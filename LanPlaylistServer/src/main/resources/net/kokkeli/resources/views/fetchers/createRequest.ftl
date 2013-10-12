<#assign header = "Fetch requests">
<#assign tab = 3>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form method="POST" class="value-fields">
					<div class="field">
						<div class="description">
							Handler: 
						</div>
						<div class=value>
							<input type="text" name="handler">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Location:
						</div>
						<div class="value">
							<input type="text" name="location">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Destination:
						</div>
						<div class="value">
							<input type="text" name="destination">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Artist:
						</div>
						<div class="value">
							<input type="text" name="artist">
						</div>
					</div>
					<div class="field">
						<div class="description">
							Track name:
						</div>
						<div class="value">
							<input type="text" name="trackname">
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
<#assign header = "Fetch requests">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<form method="POST" class="value-fields" id="form">
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
					<div class="field">
						<div class="description">
							Playlist:
						</div>
						<div class="value">
							<select name="playlist" form="form">
								<#list getModel.getItems as item>
									<option value="${item.getId}">${item.getName}</option>
								</#list>
							</select>
						</div>
					</div>
					<div class="submit-box">
						<input class="btn" type="submit" value="Create">
						<a class="btn" href="/fetchers">Cancel</a>
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
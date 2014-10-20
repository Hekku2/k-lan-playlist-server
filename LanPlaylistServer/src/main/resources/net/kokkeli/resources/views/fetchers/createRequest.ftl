<#assign header = "Create fetch request">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<form method="POST" class="form-horizontal" id="form">
				<@valueField label="Handler" name="handler" />
				<@valueField label="Location" name="location" />
				<@valueField label="Destination" name="destination" />
				<@valueField label="Artist" name="artist" />
				<@valueField label="Track name" name="track" />
				<div class="form-group">
					<label class="col-md-2 control-label">Playlist</label>
					<div class="col-md-4">
						<select class="form-control" name="selectedplaylistid">
							<#list getModel.getItems as item>
								<option value="${item.getId}">${item.getName}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="submit-box">
					<input class="btn btn-primary" type="submit" value="Create">
					<a class="btn btn-default" href="/fetchers">Cancel</a>
				</div>
			</form>
		</div>
	</body>
</html> 
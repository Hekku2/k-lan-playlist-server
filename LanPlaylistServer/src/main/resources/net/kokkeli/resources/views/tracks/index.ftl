<#assign header = "Tracks">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<script type="text/javascript" src="/resource/js/tracks.js"></script>
	
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<#if getModel.getItems??>
				<table class="table table-striped">
					<tr>
						<th>Artist</th>
						<th>Track</th>
						<th></th>
					</tr>
					<#list getModel.getItems as item>
						<tr class="<#if item.getExists>success<#else>error</#if>">
							<td>${item.getArtist}</td>
							<td>${item.getTrack}</td>
							<td>
								<button class="btn btn-default"><span class="glyphicon glyphicon-plus-sign" id="${item.getId}"></span></button>
								<a title="Edit" class="btn btn-default" href="/tracks/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
							</td>
						</tr>
					</#list>
				</table>
			<#else>
				<p>No tracks available.</p>
			</#if>
		</div>
	</body>
</html> 
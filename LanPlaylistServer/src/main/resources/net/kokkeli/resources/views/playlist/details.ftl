<#assign header = "Playlist details">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<script type="html" id="row-template">
		<tr>
			<td>
				{artist}
			</td>
			<td>
				{track}
			</td>
			<td>
				{oploader}
			</td>
			<td class="col-md-1">
				<a class="btn btn-default col-md-6" title="Edit" href="/tracks/{id}"><span class="glyphicon glyphicon-edit"></span></a>
				<button title="Delete" class="btn btn-default col-md-6" data-track-id="{id}" data-method="delete"><span class="glyphicon glyphicon-remove"></span></a>
			</td>
		</tr>
	</script>
	
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<#include "/common/_playlist.ftl">
			<div>
				<a class="btn btn-default" href="/playlists">Back</a>
			</div
		</div>
	</body>
</html> 
<#assign header = "Playlists">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<script type="text/javascript" src="/resource/js/playlists.js"></script>
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<a class="btn btn-default" href="/playlists/create">Create a playlist</a>
			<#if getModel?? && getModel.getItems??>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Name</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<#list getModel.getItems as item>
							<tr>
								<td>${item.getName}</td>
								<td>
									<a class="btn btn-default" href="/playlists/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
									<button class="btn btn-default select-playlist" id="${item.getId}"><span class="glyphicon glyphicon-play"></span></button>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
			<#else>
				No playlists found.
			</#if>
		</div>
	</body>
</html> 
<#assign header = "Playlist details">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<script type="text/javascript" src="/resource/js/playlist.js"></script>
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<form class="form-horizontal">
				<input type="hidden" id="playlist-id" value="${getModel.getId}"></input>
				<@uneditableValueField label="Name" value=getModel.getName id="name" />
				<a class="btn btn-default" href="/playlists/add/upload/${getModel.getId}">Add song</a>
				<div class="playlist-values">			
					<#if getModel.getItems??>
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Artist</th>
								<th>Track</th>
								<th>Uploader</th>
								<th>Man.</th>
							</tr>
						</thead>
						<tbody>
							<#list getModel.getItems as item>
								<tr>
									<td>
										${item.getArtist}
									</td>
									<td>
										${item.getTrack}
									</td>
									<td>
										${item.getUploader}
									</td>
									<td class="col-md-1">
										<a class="btn btn-default col-md-6" title="Edit" href="/tracks/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
										<button title="Delete" class="btn btn-default col-md-6" data-track-id="${item.getId}" data-method="delete"><span class="glyphicon glyphicon-remove"></span></a>
									</td>
								</tr>
							</#list>
						</tbody>
					</table>
					<#else>
						No tracks found.
					</#if>
				</div>
			</form>
			<div>
				<a class="btn btn-default" href="/playlists">Back</a>
			</div
		</div>
	</body>
</html> 
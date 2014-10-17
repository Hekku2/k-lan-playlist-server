<#assign header = "Playlist details">
<#assign tab = 2>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/Playlist.css">
	<script type="text/javascript" src="/resource/js/playlist.js"></script>
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<div class="playlist-info">
					<strong>Name:</strong>
						${getModel.getName}
				</div>
				<a class="btn" href="/playlists/add/upload/${getModel.getId}">Add song</a>
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
								<td>
									<a class="btn" title="Edit" href="/tracks/${item.getId}"><i class="icon-edit"></i></a>
									<a title="Delete" class="btn delete" id="${item.getId}" href="/playlists/delete/${getModel.getId}"><i class="icon-remove"></i></a>
								</td>
							</tr>
						</#list>
					</table>
					<#else>
						No tracks found.
					</#if>
					
				</div>
				<div class="navi">
					<a class="btn" href="/playlists">Back</a>
				</div>
			</div>
		</div>
	</body>
</html> 
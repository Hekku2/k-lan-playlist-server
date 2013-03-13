<#assign header = "Playlist details">
<#assign tab = 2>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/Playlist.css">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<div class="field">
					<div class="description">
						Name: 
					</div>
					<div class="value">
						${getModel.getName}
					</div>
				</div>
				<div class="playlist-values">			
					<#if getModel.getItems??>
					<div class="playlist-header">
						<div class="column-header">
							Artist 
						</div>
						<div class="column-header">
							Track
						</div>
						<div class="column-header">
							Uploader
						</div>
						<div class="column-header">
							Man.
						</div>
					</div>
					<div class="song-rows">
						<#list getModel.getItems as item>
							<div class="song-row">
								<div>
									${item.getArtist}
								</div>
								<div>
									${item.getTrackName}
								</div>
								<div>
									${item.getUploader}
								</div>
								<div>
									<a href="/tracks/${item.getId}"><img src="/resource/images/details-icon-20x20.png"/></a>
									<a href="/playlist/delete?trackId=${item.getId}?playlistId=${getModel.getId}">Del.</a>
								</div>
							</div>
						</#list>
					</div>
					<#else>
						No tracks found.
					</#if>
					
				</div>
				<div class="navi">
					<a class="button" href="/playlists">Back</a>
				</div>
			</div>
		</div>
	</body>
</html> 
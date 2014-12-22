<script type="text/javascript" src="/resource/js/playlist.js"></script>
<script type="html" id="row-template">
	<tr>
		<td>
			{artist}
		</td>
		<td>
			{track}
		</td>
		<td>
			{uploader}
		</td>
		<#if getUserRole == 3>
			<td class="col-md-1">
				<a class="btn btn-default col-md-6" title="Edit" href="/tracks/{id}"><span class="glyphicon glyphicon-edit"></span></a>
				<button title="Delete" class="btn btn-default col-md-6" 
					data-track-id="{id}"
					data-method="delete"
					data-loading-text="Loading...">
					<span class="glyphicon glyphicon-remove"></span></a>
			</td>
		</#if>
	</tr>
</script>

<form class="form-horizontal">
	<input type="hidden" id="playlist-id" value="${getModel.getId}"></input>
	<@uneditableValueField label="Name" value="" id="name" />
	<a class="btn btn-default" href="/playlists/add/upload/${getModel.getId}">Add song</a>
	<div class="playlist-values">			
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>Artist</th>
					<th>Track</th>
					<th>Uploader</th>
					<#if getUserRole == 3>
						<th>Man.</th>
					</#if>
				</tr>
			</thead>
			<tbody id="playlist-rows"></tbody>
		</table>
	</div>
</form>
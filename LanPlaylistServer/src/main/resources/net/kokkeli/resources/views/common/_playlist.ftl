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
			{oploader}
		</td>
		<td class="col-md-1">
			<a class="btn btn-default col-md-6" title="Edit" href="/tracks/{id}"><span class="glyphicon glyphicon-edit"></span></a>
			<button title="Delete" class="btn btn-default col-md-6" data-track-id="{id}" data-method="delete"><span class="glyphicon glyphicon-remove"></span></a>
		</td>
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
					<th>Man.</th>
				</tr>
			</thead>
			<tbody id="playlist-rows"></tbody>
		</table>
	</div>
</form>
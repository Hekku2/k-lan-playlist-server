<script type="text/javascript" src="/resource/js/playlists.js"></script>
<div>
	<p><strong>Current playlist: ${getModel.getName}</strong></p>
	<table class="table table-striped">
		<tr>
			<th>Artist</th>
			<th>Track</th>
			<th></th>
		</tr>
		<#list getModel.getItems as item>
			<tr>
	  			<td>${item.getArtist}</td>
	  			<td>${item.getTrack}</td>
	  			<td>
	  				<a title="Edit" class="btn btn-default" href="/tracks/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
	  				<a title="Delete" class="btn btn-default delete" id="${item.getId}" href="/playlists/delete/${getModel.getId}"><span class="glyphicon glyphicon-remove"></span></a>
	  			</td>
	  		</tr>
		</#list>
	</table>
<div>
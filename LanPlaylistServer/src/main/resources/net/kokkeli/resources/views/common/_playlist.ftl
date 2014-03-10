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
	  				<a title="Edit" class="btn" href="/tracks/${item.getId}"><i class="icon-edit"></i></a>
	  				<a title="Delete" class="btn delete" id="${item.getId}" href="/playlists/delete/${getModel.getId}"><i class="icon-remove"></i></a>
	  			</td>
	  			
	  		</tr>
		</#list>
		
	</table>
	<ul>

	</ul>
<div>
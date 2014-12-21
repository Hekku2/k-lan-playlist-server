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
	  			<td class="col-md-5">${item.getArtist}</td>
	  			<td class="col-md-6">${item.getTrack}</td>
	  			<td class="col-md-1">
	  				<#if getUserRole == 3>
		  				<a title="Edit" class="btn btn-default col-md-6" href="/tracks/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
		  				<a title="Delete" class="btn btn-default delete col-md-6" id="${item.getId}" href="/playlists/delete/${getModel.getId}"><span class="glyphicon glyphicon-remove"></span></a>
					</#if>
	  			</td>
	  		</tr>
		</#list>
	</table>
<div>
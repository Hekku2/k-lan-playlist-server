<script type="text/javascript" src="/resource/js/playlist.js"></script>
<div>
	<input type="hidden" id="playlist-id" value="${getModel.getId}"></input>
	<p><strong>Current playlist: ${getModel.getName}</strong></p>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Artist</th>
				<th>Track</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<#list getModel.getItems as item>
				<tr>
		  			<td class="col-md-5">${item.getArtist}</td>
		  			<td class="col-md-6">${item.getTrack}</td>
		  			<td class="col-md-1">
		  				<#if getUserRole == 3>
			  				<a title="Edit" class="btn btn-default col-md-6" href="/tracks/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
			  				<button title="Delete" class="btn btn-default col-md-6" data-track-id="${item.getId}" data-method="delete"><span class="glyphicon glyphicon-remove"></span></a>
						</#if>
		  			</td>
		  		</tr>
			</#list>
		</tbody>
	</table>
<div>
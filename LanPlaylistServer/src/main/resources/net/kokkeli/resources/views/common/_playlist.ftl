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
	  			<td>${item.getTrackName}</td>
	  			<td><a class="btn" href="/tracks/${item.getId}"><i class="icon-edit"></i></a></td>
	  		</tr>
		</#list>
		
	</table>
	<ul>

	</ul>
<div>
<div>
	<p>Current playlist: ${getModel.getName}</p>
	<table class="table table-striped">
		<tr>
			<th>Artist</th>
			<th>Track</th>
			<th>Edit</th>
		</tr>
		<#list getModel.getItems as item>
			<tr>
	  			<td>${item.getArtist}</td>
	  			<td>${item.getTrackName}</td>
	  			<td>edit</td>
	  		</tr>
		</#list>
		
	</table>
	<ul>

	</ul>
<div>
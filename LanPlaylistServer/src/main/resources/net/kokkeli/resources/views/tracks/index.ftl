<#assign header = "Tracks">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<script type="text/javascript" src="/resource/js/tracks.js"></script>
	
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<#if getModel.getItems??>
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
							<tr class="<#if item.getExists>success<#else>danger</#if>">
								<td>${item.getArtist}</td>
								<td>${item.getTrack}</td>
								<td class="col-md-1">
									<button class="btn btn-default col-md-6"><span class="glyphicon glyphicon-plus-sign" id="${item.getId}"></span></button>
									<a title="Edit" class="btn btn-default col-md-6" href="/tracks/${item.getId}"><span class="glyphicon glyphicon-edit"></span></a>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
			<#else>
				<p>No tracks available.</p>
			</#if>
		</div>
	</body>
</html> 
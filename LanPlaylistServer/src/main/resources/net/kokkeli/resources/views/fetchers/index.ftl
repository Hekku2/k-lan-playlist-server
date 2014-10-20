<#assign header = "Fetch requests">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/FetchRequests.css">
	<script type="html" id="row-template">
		<tr>
			<td>{handler}</td>
			<td>
				<a title="{track}" href="/tracks/{trackId}">{track}</a>
			</td>
			<td class="location-column" title="{location}">
				<div>{location}</div>
			</td>
			<td title="{destination}">
				<div >
					{destination}
				</div>
			</td>
			<td>{status}</td>
			<td>
				<a title="Remove" class="btn delete" id="{id}"><i class="icon-remove"></i>
				</a>
			</td>
		</tr>
	</script>
	
	<script type="text/javascript" src="/resource/js/fetchers.js"></script>
	<body>
		<div class="container">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<#include "/common/_info_error.ftl">
			<a class="btn btn-default" href="fetchers/createRequest">Create</a>
			<a class="btn btn-default" id="remove-handled" href="fetchers/removeHandled">Remove handled</a>
			<table class="table table-striped">
				<thead>
					<tr>
						<th>Type</th>
						<th>Track</th>
						<th>Location</th>
						<th>Destination</th>
						<th>Status</th>
						<th></th>
					</tr>
				</thead>
				<tbody id="request-table">
				</tbody>
			</table>
		</div>
	</body>
</html> 
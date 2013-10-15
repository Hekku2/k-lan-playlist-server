<#assign header = "Fetch requests">
<#assign tab = 3>

<html>
	<#include "/common/_header.ftl">
	<script type="text/javascript" src="/resource/js/fetchers.js"></script>
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<a class="button" href="fetchers/createRequest">Create</a>
				<a class="button" id="remove-handled" href="fetchers/removeHandled">Remove handled</a>
				<table class="table table-striped">
					<tr>
						<th>Type</th>
						<th>Track</th>
						<th>Location</th>
						<th>Destination</th>
						<th>Status</th>
						<th></th>
					</tr>
					<tbody id="request-table">
					</tbody>
				</table>
			</div>
		</div>
	</body>
</html> 
<#assign header = "Fetch requests">
<#assign tab = 3>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<a class="button" href="fetchers/createRequest">Create</a>
				<#if getModel.getItems??>
					<table class="table table-striped">
						<tr>
							<th>Type</th>
							<th>Track</th>
							<th>Location</th>
							<th>Destination</th>
							<th>Status</th>
						</tr>
						<#list getModel.getItems as item>
							<tr>
								<td>${item.getHandler}</td>
								<td><a title="${item.getTrack}" href="/tracks/${item.getTrackId}">${item.getTrack}</a></td>
								<td>${item.getLocation}</td>
								<td>${item.getDestination}</td>
								<td>${item.getStatus}</td>
							</tr>
						</#list>
					</table>
				<#else>
					<p> No fetch requests. </p>
				</#if>
			</div>
		</div>
	</body>
</html> 
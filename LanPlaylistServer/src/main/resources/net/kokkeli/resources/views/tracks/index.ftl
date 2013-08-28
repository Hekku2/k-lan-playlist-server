<#assign header = "Tracks">
<#assign tab = 0>

<html>
	<#include "/common/_header.ftl">
	
	<script type="text/javascript" src="/resource/js/tracks.js"></script>
	
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<#if getModel.getItems??>
					<table class="table table-striped">
						<tr>
							<th>Artist</th>
							<th>Track</th>
							<th></th>
						</tr>
						<#list getModel.getItems as item>
							<tr class="<#if item.getExists>success<#else>error</#if>">
								<td>${item.getArtist}</td>
								<td>${item.getTrackName}</td>
								<td><button class="add-track btn" id="${item.getId}">+</button></td>
							</tr>
						</#list>
					</table>
				<#else>
					<p>No tracks available.</p>
				</#if>
			</div>
		</div>
	</body>
</html> 
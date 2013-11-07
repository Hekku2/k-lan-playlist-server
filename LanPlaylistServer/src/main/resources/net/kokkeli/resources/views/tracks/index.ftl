<#assign header = "Tracks">
<#assign tab = 3>
<!DOCTYPE HTML>

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
								<td>
									<button class="btn"><i class="icon-plus-sign" id="${item.getId}"></i></button>
									<a title="Edit" class="btn" href="/tracks/${item.getId}"><i class="icon-edit"></i></a>
								</td>
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
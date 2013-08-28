<#assign header = "Playlists">
<#assign tab = 2>

<html>
	<#include "/common/_header.ftl">
	<script type="text/javascript" src="/resource/js/playlists.js"></script>
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<a class="button" href="/playlists/create">Create a playlist</a>
				<#if getModel?? && getModel.getItems??>
					<table class="table table-striped">
						<tr>
							<th>Name</th>
							<th>Man.<th>
						</tr>
						<#list getModel.getItems as item>
							<tr>
								<td>${item.getName}</td>
								<td>
									<a href="/playlists/${item.getId}"><img src="/resource/images/details-icon-20x20.png"/></a>
									<button class="btn select-playlist" id="${item.getId}">Play</button>
								</td>
							</tr>
						</#list>
					</table>
				<#else>
					No playlists found.
				</#if>
			</div>
		</div>
	</body>
</html> 
<#assign header = "Playlists">
<#assign tab = 2>
<!DOCTYPE HTML>

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
				<a class="btn" href="/playlists/create">Create a playlist</a>
				<#if getModel?? && getModel.getItems??>
					<table class="table table-striped">
						<tr>
							<th>Name</th>
							<th></th>
						</tr>
						<#list getModel.getItems as item>
							<tr>
								<td>${item.getName}</td>
								<td>
									<a class="btn" href="/playlists/${item.getId}"><i class="icon-edit"></i></a>
									<button class="btn select-playlist" id="${item.getId}"><i class="icon-play"></i></button>
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
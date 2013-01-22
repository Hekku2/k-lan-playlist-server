<#assign header = "Playlists">
<#assign tab = 2>

<html>
	<#include "/common/_header.ftl">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#if getModel?? && getModel.getItems??>
					<table>
						<tr>
							<th>Name</th>
							<th>Man.<th>
						</tr>
						<#list getModel.getItems as item>
							<tr>
								<td>${item.getName}</td>
								<td><a href="/playlists/${item.getId}">Details</a></td>
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
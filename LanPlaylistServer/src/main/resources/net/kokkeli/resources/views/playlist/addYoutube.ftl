<#assign header = "Add to playlist">
<#assign tab = 2>
<#assign selectedMethod = 1>

<html>
	<#include "/common/_header.ftl">
	<link rel="stylesheet" type="text/css" href="/resource/css/PlaylistAdd.css">
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<#include "/playlist/_method.ftl">
				<form action="" enctype="multipart/form-data" method="post">
					<div class="field">
						<div class="description">
							Artist: 
						</div>
						<div class="value">
							<input type="text" name="artist">
						</div>
					</div>
					<div>
						<input type="submit" value="Send">
					</div>
				</form>
			</div>
		</div>
	</body>
</html> 
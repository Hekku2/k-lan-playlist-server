<#assign header = "Logs">
<#assign tab = 3>
<!DOCTYPE HTML>

<html>
	<#include "/common/_header.ftl">
	
	<script type="html" id="row-template">
		<tr>
			<td>{severity}</td>
			<td>{timestamp}</td>
			<td>{message}</td>
			<td>{source}</td>
		</tr>
	</script>
	
	<script type="text/javascript" src="/resource/js/log.js"></script>
	<body>
		<div class="inner-body">
			<#include "/common/_topsection.ftl">
			<#include "/common/_playing.ftl">
			<h1>${header}</h1>
			<div class="content">
				<#include "/common/_info_error.ftl">
				<table class="table table-striped">
					<tr>
						<th>Severity</th>
						<th>Time</th>
						<th>Message</th>
						<th>Source</th>
					</tr>
					<tbody id="log-table">
					</tbody>
				</table>
			</div>
		</div>
	</body>
</html> 
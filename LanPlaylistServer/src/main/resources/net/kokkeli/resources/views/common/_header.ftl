<#macro uneditableValueField label value>
	<div class="control-group">
		<div class="control-label">${label}</div>
		<div class="controls">
			<span class="input-xlarge uneditable-input">${value}</span>
		</div>
	</div>
</#macro>

<#macro valueField label name>
	<div class="control-group">
		<div class="control-label">${label}</div>
		<div class="controls">
			<input class="input-xlarge" name="${name}"/>
		</div>
	</div>
</#macro>

<head>
	<link rel="stylesheet" type="text/css" href="/resource/css/bootstrap/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="/resource/css/bootstrap/bootstrap-responsive.css">
	<link rel="stylesheet" type="text/css" href="/resource/css/Common.css">
	<link rel="stylesheet" type="text/css" href="/resource/css/TopSection.css">
	
	<script type="text/javascript" src="/resource/js/jquery/jquery-2.0.3.js"></script>
	<script type="text/javascript" src="/resource/js/bootstrap/bootstrap.js"></script>
	<script type="text/javascript" src="/resource/js/nano/nano.js"></script>
	
	<title>${header}</title>
</head>
<#macro uneditableValueField label value>
	<div class="control-group">
		<div class="control-label">${label}</div>
		<div class="controls">
			<span class="input-xlarge uneditable-input">${value}</span>
		</div>
	</div>
</#macro>

<#macro valueField label name value="">
	<div class="control-group">
		<label class="control-label" for="${name}">${label}</label>
		<div class="controls">
			<input type="text" class="input-xlarge" name="${name}" value="${value}"/>
		</div>
	</div>
</#macro>

<#macro passwordField label name>
	<div class="control-group">
		<label class="control-label" for="${name}">${label}</label>
		<div class="controls">
			<input type="password" class="input-xlarge" name="${name}" value=""/>
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
	<script type="text/javascript" src="/resource/js/moment/moment.min.js"></script>
	
	<title>${header}</title>
</head>
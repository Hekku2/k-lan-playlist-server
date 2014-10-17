<#macro uneditableValueField label value>
	<div class="form-group">
		<label class="col-md-2 control-label">${label}</label>
		<div class="col-md-4">
			<p class="form-control-static">${value}</p>
		</div>
	</div>
</#macro>

<#macro valueField label name value="">
	<div class="form-group">
		<label for="${name}" class="col-md-2 control-label">${label}</label>
		<div class="col-md-4">
			<input type="text" class="form-control" name="${name}" value="${value}">
		</div>
	</div>
</#macro>

<#macro passwordField label name>
	<div class="form-group">
		<label for="${name}" class="col-md-2 control-label">${label}</label>
		<div class="col-md-4">
			<input type="password" class="form-control" name="${name}" value="">
		</div>
	</div>
</#macro>

<head>
	<link rel="stylesheet" type="text/css" href="/resource/css/bootstrap/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="/resource/css/bootstrap/bootstrap-responsive.css">
	
	<script type="text/javascript" src="/resource/js/jquery/jquery-2.0.3.js"></script>
	<script type="text/javascript" src="/resource/js/bootstrap/bootstrap.js"></script>
	<script type="text/javascript" src="/resource/js/nano/nano.js"></script>
	<script type="text/javascript" src="/resource/js/moment/moment.min.js"></script>
	
	<title>${header}</title>
</head>
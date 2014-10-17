<#if getInfo??>
	<div class="alert alert-info" role="alert">
		${getInfo}
	</div>
</#if>
<#if getError??>
	<div class="alert alert-danger" role="alert"> ${getError}</div>
</#if>
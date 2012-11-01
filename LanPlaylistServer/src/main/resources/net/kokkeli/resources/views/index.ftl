<#assign header = "Main page">

<html>
	<#include "common/_header.ftl">
	<body>
		<#include "common/_topsection.ftl">
		<h1>${header}</h1>
		<p>${getName}</p>
		
		<div class="content">
			<ul>
				<#list getItems as item>
				  <li>${item.getName}
				</#list>
			</ul>  	
		</div>
	</body>
</html> 
<#assign header = "Authentication">

<html>
	<#include "common/_header.ftl">
	<body>
		<#include "common/_topsection.ftl">
		<h1>${header}</h1>
		<div class="content">
			<form name="input" action="authentication" method="post">
			Username: <input type="text" name="user">
			Password: <input type="password" name="pwd">
			<input type="submit" value="Submit">
			</form>
		</div>
	</body>
</html> 
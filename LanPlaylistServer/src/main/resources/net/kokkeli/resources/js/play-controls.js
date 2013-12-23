$(document).ready(function(){
	$("#play").click(function () {
		$.post('/play', {});
	});
	
	$("#pause").click(function () {
		$.post('/pause', {});
	});
});
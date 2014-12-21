$(document).ready(function(){
	$("#play").click(function () {
		$.post('/player/play', {});
	});
	
	$("#pause").click(function () {
		$.post('/player/pause', {});
	});
});
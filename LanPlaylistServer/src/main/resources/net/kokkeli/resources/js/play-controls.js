$(document).ready(function(){
	$("#play").click(function () {
		
		$.post('/play', {"jee": 2});
		
	});
});
$(document).ready(function(){
	$("button[class*='add-track']").click(function () {
		
		$.post('/play', { "id": 1 });
		
	});
});
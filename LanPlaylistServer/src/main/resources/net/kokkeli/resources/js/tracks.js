$(document).ready(function(){
	$("button[class*='icon-plus-sign']").click(function (event) {
		$.post('/addToQueue', { "id": $(event.target).attr('id') });
		
	});
});
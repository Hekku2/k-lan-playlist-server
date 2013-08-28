$(document).ready(function(){
	$("button[class*='add-track']").click(function (event) {
		$.post('/addToQueue', { "id": $(event.target).attr('id') });
		
	});
});
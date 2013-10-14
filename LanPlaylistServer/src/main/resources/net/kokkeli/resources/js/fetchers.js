$(document).ready(function(){
	$("#remove-handled").click(function (event) {
		var element = $(event.currentTarget);
		$.post(element.attr("href"), function(data){
			document.location.reload(true);
		});
		return false;
	});
	
	$("a[class*='delete']").click(function (event) {
		var element = $(event.currentTarget);
		$.post('fetchers/removeRequest', { "id": element.attr('id')}, function(data){
			document.location.reload(true);
		});
		return false;
	});
});
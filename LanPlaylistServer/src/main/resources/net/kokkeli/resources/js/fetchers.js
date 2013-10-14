$(document).ready(function(){
	$("#remove-handled").click(function (event) {
		var element = $(event.currentTarget);
		$.post(element.attr("href"), function(data){
			document.location.reload(true);
		});
		return false;
	});
});
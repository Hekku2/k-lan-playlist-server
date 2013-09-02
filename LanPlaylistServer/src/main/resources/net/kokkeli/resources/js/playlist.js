$(document).ready(function(){
	$("a[class*='delete']").click(function (event) {
		var element = $(event.target);
		$.post(element.attr("href"), { "id": element.attr('id') });
		return false;
	});
});
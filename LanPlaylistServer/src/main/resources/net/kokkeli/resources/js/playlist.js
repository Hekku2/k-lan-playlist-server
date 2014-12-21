$(document).ready(function(){
	$("a[class*='delete']").click(deleteItem);
});

function deleteItem(event){
	var element = $(event.currentTarget);
	$.post(element.attr("href"), { "id": element.attr('id') }, function() {
		document.location.reload(true);
	});
	return false;
}
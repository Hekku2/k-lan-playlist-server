//TODO Add timer for updating.
var rowTemplate;

$(document).ready(function(){
	$("#remove-handled").click(function (event) {
		var element = $(event.currentTarget);
		$.post(element.attr("href"), function(data){
			updateRequests();
		});
		return false;
	});
	
	$(document).on("click", "#request-table a[class*='delete']", function (event) {
		var element = $(event.currentTarget);
		$.post('fetchers/removeRequest', { "id": element.attr('id')}, function(data){
			updateRequests();
		});
		return false;
	});
	
	rowTemplate = $("#row-template").text();
	updateRequests();
});

function updateRequests(){
	$.get('fetchers/requests', function(data){
		$("#request-table").html('');
		
		$.each(data.items, function(i, item){
			$("#request-table").append(nano(rowTemplate, item))
		})
	});
}
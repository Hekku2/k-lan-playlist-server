var rowTemplate = "<tr><td>{handler}</td><td><a title=\"{track}\" href=\"/tracks/{trackId}\">{track}</a></td><td>{location}</td><td>{destination}</td><td>{status}</td><td><a title=\"Remove\" class=\"btn delete\" id=\"{id}\"><i class=\"icon-remove\"></i></a></td></tr>"
//TODO Find proper place for template.
//TODO Add timer for updating.
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
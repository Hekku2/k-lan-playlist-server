var rowTemplate;

$(document).ready(function(){
	rowTemplate = $("#row-template").text();
	updateRequests();
});

/*
 * Updates requests table
 */
function updateRequests(){
	$.get('log/logs', function(data){
		$("#log-table").html('');
		
		$.each(data, function(i, item){
			item.timestamp = moment(item.timestamp).format('YYYY-MM-DD HH:mm:ss');
			$("#log-table").append(nano(rowTemplate, item))
		})
	});
}
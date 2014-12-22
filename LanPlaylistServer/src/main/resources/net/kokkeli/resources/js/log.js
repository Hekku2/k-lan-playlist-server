var rowTemplate;

$(document).ready(function(){
	rowTemplate = $("#row-template").text();
	getLogRows();
});

/*
 * Updates requests table
 */
function getLogRows(){
	$.get('log/logs').done(updateTableRows);
}

function updateTableRows(data){
	emptyRows();
	$.each(data, function(i, item){
		item.timestamp = moment(item.timestamp).format('YYYY-MM-DD HH:mm:ss');
		$("#log-table").append(nano(rowTemplate, item))
	})
}

function emptyRows(){
	$("#log-table").html('');
}
var tableBodySelector = "#playlist-rows";

var rowTemplate;
$(document).ready(function(){
	rowTemplate = $("#row-template").text();
	$(tableBodySelector).on("click", "button[data-method='delete']", deleteItem);
	getRefreshedData();
});

function deleteItem(event){
	var element = $(event.currentTarget);
	$.post("/playlists/removeTrackFromPlaylist/" + $("#playlist-id").val(), buildData(element)).done(getRefreshedData);
	return false;
}

function buildData(element){
	return { "id": element.attr("data-track-id") };
}
function getRefreshedData(){
	$.get("/playlists/playlist/"+ $("#playlist-id").val()).done(updateTableRows);
}

function updateTableRows(data){
	emptyRows();
	$("#name").text(data.name);
	$.each(data.items, function(i, item){
		$(tableBodySelector).append(nano(rowTemplate, item))
	})
}

function emptyRows(){
	$(tableBodySelector).html('');
}
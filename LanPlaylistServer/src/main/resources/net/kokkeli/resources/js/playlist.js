var tableBodySelector = "#playlist-rows";
var playlistId;

var rowTemplate;
$(document).ready(function(){
	playlistId = $("#playlist-id").val();
	
	rowTemplate = $("#row-template").text();
	$(tableBodySelector).on("click", "button[data-method='delete']", deleteItem);
	getRefreshedData();
});

function deleteItem(event){
	$(this).button('loading')
	var element = $(event.currentTarget);
	$.post("/playlists/removeTrackFromPlaylist/" + playlistId, buildData(element)).done(showSuccess).fail(showError).always(getRefreshedData);
	return false;
}

function buildData(element){
	return { "id": element.attr("data-track-id") };
}

function getRefreshedData(){
	$.get("/playlists/playlist/"+ playlistId).done(updateTableRows);
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
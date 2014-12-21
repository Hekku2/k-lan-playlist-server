$(document).ready(function(){
	$("button[data-method='delete']").click(deleteItem);
});

function deleteItem(event){
	var element = $(event.currentTarget);
	$.post("/playlists/removeTrackFromPlaylist/" + $("#playlist-id").val(), buildData(element)).done(refreshPage);
	return false;
}

function buildData(element){
	return { "id": element.attr("data-track-id") };
}

function refreshPage(){
	document.location.reload(true);
}
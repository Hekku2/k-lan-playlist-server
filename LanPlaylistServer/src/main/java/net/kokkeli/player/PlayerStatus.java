package net.kokkeli.player;

public class PlayerStatus {
    private boolean isPlaying;
    private boolean readyForPlay;
    private String title;
    private long selectedPlaylist;
    
    public boolean getPlaying(){
        return isPlaying;
    }
    
    public void setPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public long getSelectedPlaylist() {
        return selectedPlaylist;
    }

    public void setSelectedPlaylist(long selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    public boolean getReadyForPlay() {
        return readyForPlay;
    }

    public void setReadyForPlay(boolean readyForPlay) {
        this.readyForPlay = readyForPlay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

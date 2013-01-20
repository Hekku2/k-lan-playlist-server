package net.kokkeli.player;

public class MockPlayer implements IPlayer {

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }

    @Override
    public String getTitle() {
        return "MockTitle";
    }

    @Override
    public long getCurrentPlaylistId() {
        return 1;
    }
}

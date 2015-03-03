package net.kokkeli.player;

import net.kokkeli.data.services.ServiceException;

public class MockPlayer implements IPlayer {

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }
    
    @Override
    public void addToQueue(String file) {
    }

    @Override
    public void selectPlaylist(long id) {
    }

    @Override
    public PlayerStatus status() throws ServiceException {
        PlayerStatus status = new PlayerStatus();
        status.setPlaying(true);
        status.setReadyForPlay(true);
        status.setSelectedPlaylist(1);
        status.setTitle("MockTitle");
        return status;
    }
}

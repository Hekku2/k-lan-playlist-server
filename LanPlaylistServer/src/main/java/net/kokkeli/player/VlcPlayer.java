package net.kokkeli.player;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VlcPlayer implements IPlayer {
    private final AudioMediaPlayerComponent player;
    
    /**
     * Creates new Player
     * @param libLocation Location of 
     */
    public VlcPlayer(ISettings setting, ILogger logger){
        
        String vlcLibName = RuntimeUtil.getLibVlcLibraryName();
        NativeLibrary.addSearchPath(vlcLibName, setting.getLibLocation());
        Native.loadLibrary(vlcLibName, LibVlc.class);
        
        player = new PlayerComponent(logger);
        
    }
    
    public void playFile(String file) throws InterruptedException{
        player.getMediaPlayer().playMedia(file);
    }

    @Override
    public void play() {
        
    }

    @Override
    public void pause() {
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public long getCurrentPlaylistId() throws NotPlaylistPlayingException {
        return 0;
    }

    private class PlayerComponent extends AudioMediaPlayerComponent{
        private final ILogger logger;
        
        public PlayerComponent(ILogger logger){
            this.logger = logger;
        }
        
        @Override
        public void opening(MediaPlayer mediaPlayer) {
            super.opening(mediaPlayer);
            logger.log("Opening message.", LogSeverity.TRACE);
            System.out.println("Opening");
        }
        
        @Override
        public void stopped(MediaPlayer mediaPlayer) {
            super.stopped(mediaPlayer);
            System.out.println("Stopped");
        }
        
        public void finished(MediaPlayer player){
            System.out.println("Finished");
        }
        
        @Override
        public void playing(MediaPlayer mediaPlayer) {
            // TODO Auto-generated method stub
            super.playing(mediaPlayer);
            System.out.println("Playing");
        }
        
        @Override
        public void error(MediaPlayer mediaPlayer) {
            System.out.println("Failed to play media");
            System.exit(1);
        }
    }
}

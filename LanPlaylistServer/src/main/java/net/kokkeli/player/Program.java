package net.kokkeli.player;

import java.io.IOException;
import java.net.URI;

import net.kokkeli.ISettings;

public class Program {

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        ISettings s = new ISettings() {
            
            @Override
            public String getVlcLocation() {
                return "D:\\vlc-2.0.5";
            }
            
            @Override
            public String getTracksFolder() {
                return null;
            }
            
            @Override
            public String getTemplatesLocation() {
                return null;
            }
            
            @Override
            public String getPasswordSalt() {
                return null;
            }
            
            @Override
            public String getLibLocation() {
                return null;
            }
            
            @Override
            public String getDatabaseLocation() {
                return null;
            }

            @Override
            public void loadSettings(String file) throws IOException {
            }

            @Override
            public String getServerUri() {
                return null;
            }

            @Override
            public URI getBaseURI() {
                return null;
            }

            @Override
            public URI getURI(String endPart) {
                return null;
            }
        };
        
        VlcPlayer player = new VlcPlayer(s, null);
        
        player.playFile("E:\\chimes.wav");
        Thread.sleep(4000);
        
    }

}

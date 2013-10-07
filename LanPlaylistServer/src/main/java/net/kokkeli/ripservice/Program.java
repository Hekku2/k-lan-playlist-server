package net.kokkeli.ripservice;

import java.io.IOException;
import net.kokkeli.ISettings;
import net.kokkeli.Settings;
import net.kokkeli.data.Logging;

/**
 * Program running rippers
 * @author Hekku2
 *
 */
public class Program {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String settingsFile = "settings/default.dat";
        
        if (args.length > 0){
            settingsFile = args[0];
        }
        
        ISettings settings = new Settings();
        try {
            settings.loadSettings(settingsFile);
        } catch (IOException e) {
            System.out.println("Unable to load settings from file " + settingsFile);
            System.out.println(e.toString());
        }
        
        YouTubeRipper ripper = new YouTubeRipper(settings, new Logging());
        ripper.fetch("http://www.youtube.com/watch?v=gOTyD6ZYcP0", "test.ogg");
    }

}

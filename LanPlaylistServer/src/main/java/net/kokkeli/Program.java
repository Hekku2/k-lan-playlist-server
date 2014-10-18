package net.kokkeli;

import java.io.IOException;

import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

public class Program {

	/**
	 * Starting point of program.
	 * @param args Used for settings file
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {    
	    LanServer server = null;
	    String settingsFile = "settings/default.dat";
		try {
		    System.setProperty("SQLite.encoding", "utf-8");
		    
		    if (args.length > 0){
		        settingsFile = args[0];
		    }
		    
	        Settings settings = new Settings();
            settings.loadSettings(settingsFile);

		    System.out.println("Starting server with settings: " + settingsFile);
		    server = new LanServer(settings);
            server.start();
            System.in.read();
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Settings file " + settingsFile + " is missings or invalid format!");
        } catch (ServerException e) {
            System.out.println(e.toString());
        } finally {
            if (server != null){
                server.stop();
            }
        }
	}
}

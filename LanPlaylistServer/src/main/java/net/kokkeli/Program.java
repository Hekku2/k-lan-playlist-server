package net.kokkeli;

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
		try {
		    System.setProperty("SQLite.encoding", "utf-8");
		    
		    String settingsFile = "settings/default.dat";
		    
		    if (args.length > 0){
		        settingsFile = args[0];
		    }
		    
		    System.out.println("Starting server with settings: " + settingsFile);
		    server = new LanServer(settingsFile);
            server.start();
            System.in.read();
        } catch (ServerException e) {
            System.out.println(e.toString());
        } finally {
            if (server != null){
                server.stop();
            }            
        }
	}
}

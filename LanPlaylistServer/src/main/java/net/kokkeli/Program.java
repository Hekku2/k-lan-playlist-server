package net.kokkeli;

import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

public class Program {

	/**
	 * Starting point of program.
	 * @param args Not used.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {    
	    LanServer server = null;
		try {
		    String settingsFile = "settings/default.dat";
		    
		    if (args.length > 0){
		        settingsFile = args[0];
		    }
		    //TODO Better error message if database doesnt exist!
		    
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

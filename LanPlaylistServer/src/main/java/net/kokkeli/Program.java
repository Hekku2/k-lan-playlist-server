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
		    System.out.println("Starting server.");
		    server = new LanServer();
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

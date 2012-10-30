package net.kokkeli;

import java.io.IOException;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

public class Program {

	/**
	 * Starting point of program.
	 * @param args Not used.
	 * @throws IOException Throws if something went wrong
	 */
	public static void main(String[] args) throws IOException {    
	    LanServer server = null;
		try {
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

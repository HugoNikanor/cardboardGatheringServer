package main;

import server.Server;

public class SteppingStone {

	public SteppingStone( String[] args ) {
		int port = 23732;
		try {
			port = Integer.parseInt(args[0]);
		} catch( IndexOutOfBoundsException ioube ) {
		}

		new Server( port );
	}
}

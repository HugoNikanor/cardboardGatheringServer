package main;

import server.Server;

public class Main {
	public static void main( String[] args ) {
		int port = 23732;
		try {
			port = Integer.parseInt(args[0]);
		} catch( IndexOutOfBoundsException ioube ) {
		}

		new Server( port );
	}
}

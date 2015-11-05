package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	//private int port;

	private ServerSocket serverSocket;
	private Socket[] connections = new Socket[2];

	private OutputStream[] outStreams = new OutputStream[2];
	private InputStream[] inStreams = new InputStream[2];

	private TransferThread[] transferThreads = new TransferThread[2];

	public Server( int port ) {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println( "custom closure" );
				close();
			}
		});

		//this.port = port;

		try {
			serverSocket = new ServerSocket( port );
			System.out.println( "Server started, waiting for cilents..." );

			connections[0] = serverSocket.accept();
			outStreams [0] = connections[0].getOutputStream();
			inStreams  [0] = connections[0].getInputStream();
			System.out.println( "First client found." );

			connections[1] = serverSocket.accept();
			outStreams [1] = connections[1].getOutputStream();
			inStreams  [1] = connections[1].getInputStream();
			System.out.println( "Secound client found.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		transferThreads[0] = new TransferThread( inStreams[0], outStreams[1] );
		transferThreads[1] = new TransferThread( inStreams[1], outStreams[0] );

		System.out.println( "transferThreads created" );

		new Thread( transferThreads[0] ).start();
		new Thread( transferThreads[1] ).start();

		System.out.println( "Server setup completed" );
	}


	/*
	 * Cleanup, closes everything
	 */
	public void close() {
		try {
			serverSocket.close();

			connections[0].close();
			connections[1].close();

			transferThreads[0].setRunning( false );
			transferThreads[1].setRunning( false );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

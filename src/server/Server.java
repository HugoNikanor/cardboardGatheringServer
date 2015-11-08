package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private int port;

	private ServerSocket serverSocket;
	private Socket[] connections = new Socket[2];

	private OutputStream[] outStreams = new OutputStream[2];
	private InputStream[] inStreams = new InputStream[2];

	private TransferThread[] transferThreads = new TransferThread[2];
	//private Thread[] transferThreads = new Thread[2];

	//private RestartThread[] restartThreads = new RestartThread[2];
	private Thread[] restartThreads = new Thread[2];

	public Server( int port ) {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println( "custom closure" );
				close();
			}
		});

		this.port = port;

	}

	public void start() {
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

		Thread tt0 = new Thread( transferThreads[0] );
		Thread tt1 = new Thread( transferThreads[1] );

		System.out.println( "transferThreads created" );

		restartThreads[0] = new Thread(new RestartThread( tt0, 0 ));
		restartThreads[1] = new Thread(new RestartThread( tt1, 1 ));

		tt0.start();
		tt1.start();

		restartThreads[0].start();
		restartThreads[1].start();

		System.out.println( "Server setup completed" );
	}

	private class RestartThread implements Runnable {
		private Thread rThread;

		private int id;
		public RestartThread( Thread threadToJoin, int id ) {
			this.rThread = threadToJoin;
			this.id = id;
		}
		@Override
		public void run() {
			try {
				rThread.join();
				System.out.println("TransferThread closed");
				Server.this.close( id );
				//Server.this.start();
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}


	public void close( int source ) {
		try {
			serverSocket.close();
			connections[0].close();
			connections[1].close();

			transferThreads[0].setRunning( false );
			transferThreads[1].setRunning( false );

			restartThreads[source].interrupt();
		} catch( IOException e ) {
			e.printStackTrace();
		}
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

			restartThreads[0].interrupt();
			restartThreads[1].interrupt();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

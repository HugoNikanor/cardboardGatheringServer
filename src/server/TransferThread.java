package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class TransferThread implements Runnable {

	private OutputStream outStream;
	private InputStream inStream;

	private boolean running;

	public TransferThread( InputStream inStream, OutputStream outStream) {
		this.outStream = outStream;
		this.inStream = inStream;

	}

	@Override
	public void run() {
		synchronized( this ) {
			running = true;
			int data;
			while( running ) {
				try {
					data = inStream.read();
					if( data != -1 ) {
						outStream.write( data );
					} else {
						System.out.println( "client disconnected" );
						running = false;
					}
				} catch( SocketException e ) {
					running = false;
					System.out.println( "Socket closed" );
				} catch (IOException e) {
					running = false;
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}

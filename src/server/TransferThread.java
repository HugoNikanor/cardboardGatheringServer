package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferThread implements Runnable {

	private InputStream inStream;
	private OutputStream outStream;

	private boolean running;

	public TransferThread( InputStream inStream, OutputStream outStream) {
		this.inStream = inStream;
		this.outStream = outStream;

	}
	public void run() {
		synchronized( this ) {
			running = true;
			while( running ) {
				try {
					outStream.write( inStream.read() );
					System.out.println( this.hashCode() + " has sent info" );

					this.wait( 1000 / 10 );
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
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

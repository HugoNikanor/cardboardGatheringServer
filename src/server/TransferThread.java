package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferThread implements Runnable {

	private OutputStream outStream;
	private InputStream inStream;

	private boolean running;

	public TransferThread( InputStream inStream, OutputStream outStream) {
		this.outStream = outStream;
		this.inStream = inStream;

	}
	public void run() {
		synchronized( this ) {
			running = true;
			while( running ) {
				try {
					outStream.write( inStream.read() );
					System.out.println( this.hashCode() + " has sent info" );

					this.wait( 1000 / 10 );
				} catch (IOException | InterruptedException e) {
					running = false;
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

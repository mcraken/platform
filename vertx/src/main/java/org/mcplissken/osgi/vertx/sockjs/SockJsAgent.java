/**
 * 
 */
package org.mcplissken.osgi.vertx.sockjs;

import java.nio.ByteBuffer;

import org.mcplissken.gateway.restful.document.DocumentWriter;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.sockjs.SockJSSocket;
/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public abstract class SockJsAgent  {

	private SockJSSocket socket;
	private DocumentWriter writer;
	
	/**
	 * @param writer the writer to set
	 */
	public void setWriter(DocumentWriter writer) {
		this.writer = writer;
	}
	
	/**
	 * @param socket the socket to set
	 */
	public void setSocket(SockJSSocket socket) {
		
		this.socket = socket;
		
		socket.dataHandler(new Handler<Buffer>() {
			
			@Override
			public void handle(Buffer event) {
				
				byte[] inputArr = event.getByteBuf().array();
				
				socketMessageReceived(inputArr);
			}
		});
		
	}
	
	protected void writeMessage(byte[] bytesBuff){
		
		socket.write(new Buffer(bytesBuff));
	}
	
	protected void writeDocument(Object document){
		
		byte[] bytesBuff = bufferDocument(document);
		
		writeMessage(bytesBuff);
		
	}

	protected byte[] bufferDocument(Object document) {
		
		ByteBuffer output = ByteBuffer.allocate(1024 * 1024);
		
		writer.write(document, output);
		
		byte[] bytesBuff = unpackBuffer(output);
		
		return bytesBuff;
	}

	private byte[] unpackBuffer(ByteBuffer output) {
		
		output.flip();
		
		int length = output.limit();

		byte[] bytesBuff = new byte[length];

		output.get(bytesBuff);
		
		output.clear();
		
		return bytesBuff;
	}
	
	protected abstract void socketMessageReceived(byte[] message);

}

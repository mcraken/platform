/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.platform.vertx.sockjsgateway;

import java.nio.ByteBuffer;
import java.util.Map;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.httpgateway.spi.BasicHttpAdapter;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.sockjs.SockJSSocket;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxSockJsAdapter extends BasicHttpAdapter {

	private SockJSSocket socket;
	
	/**
	 * @param socket
	 */
	public VertxSockJsAdapter(SockJSSocket socket) {
		this.socket = socket;
	}

	public SockJSSocket socket(){
		return socket;
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#method()
	 */
	@Override
	public String method() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#path()
	 */
	@Override
	public String path() {
		
		return socket.uri();
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#readParameter(java.lang.String)
	 */
	@Override
	public String readParameter(String name) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#writeHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeHeader(String name, String value) {
		
		socket.headers().add(name, value);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#readHeader(java.lang.String)
	 */
	@Override
	public String readHeader(String name) {
		
		return socket.headers().get(name);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#writeResponse(java.nio.ByteBuffer)
	 */
	@Override
	public void writeResponse(ByteBuffer buffer) {
		
		int length = buffer.limit();

		byte[] bytesBuff = new byte[length];

		buffer.get(bytesBuff);
		
		socket.write(new Buffer(bytesBuff));

	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#sessionId()
	 */
	@Override
	public String sessionId() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#setCookie(java.lang.String, java.lang.String)
	 */
	@Override
	public void setCookie(String name, String value) {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#getContentType()
	 */
	@Override
	public String getContentType() {
		
		String contentType = readHeader("Content-Type");
		
		if(contentType == null){
			contentType = "application/json";
		}
		
		return contentType;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#getContentLangauge()
	 */
	@Override
	public String getContentLangauge() {
		return readHeader("Content-Language");
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(String uri) {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.spi.BasicHttpAdapter#handleErrorResponse(int, java.lang.String)
	 */
	@Override
	protected void handleErrorResponse(int errorCode, String message) {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#createGatewayRequest(java.util.Map, java.lang.String)
	 */
	@Override
	public GatewayRequest createGatewayRequest(
			Map<String, DocumentReader> documentReaders) {
		
		return new VertxSockJsRequest(this, documentReaders);
	}

}

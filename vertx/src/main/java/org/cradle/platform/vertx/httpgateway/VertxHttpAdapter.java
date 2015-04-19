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
package org.cradle.platform.vertx.httpgateway;

import java.nio.ByteBuffer;
import java.util.Map;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.httpgateway.spi.BasicHttpAdapter;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.streams.Pump;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 11, 2014
 */
public class VertxHttpAdapter extends BasicHttpAdapter{
	
	private HttpServerResponse response;
	
	private HttpServerRequest request;

	public VertxHttpAdapter(HttpServerRequest request, 
			HttpServerResponse response) {

		this.response = response;
		
		this.request = request;
		
		Pump.createPump(request, response).start();

	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.gateway.HttpAdapter#readParameter(java.lang.String)
	 */
	@Override
	public String readParameter(String name) {
		return request.params().get(name);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.gateway.HttpAdapter#writeHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeHeader(String name, String value) {
		response.putHeader(name, value);

	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.gateway.HttpAdapter#readHeader(java.lang.String)
	 */
	@Override
	public String readHeader(String name) {

		return request.headers().get(name);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.gateway.HttpAdapter#writeResponse(java.nio.ByteBuffer)
	 */
	@Override
	public void writeResponse(ByteBuffer buffer) {

		int length = buffer.limit();

		byte[] bytesBuff = new byte[length];

		buffer.get(bytesBuff);
		
		response.end(new Buffer(bytesBuff));

	}

	/**
	 * @return the request
	 */
	public HttpServerRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public HttpServerResponse getResponse() {
		return response;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpAdapter#sessionId()
	 */
	@Override
	public String sessionId() {
		
		if(sessionId == null){
			
			String cookie = request.headers().get(COOKIE);
			
			if(cookie != null)
				extractSessionId(cookie);
			else
				return "";
		}
		
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpAdapter#setCookie()
	 */
	@Override
	public void setCookie(String name, String value) {
		
		String cookie = response.headers().get(COOKIE);
		
		cookie = createCookie(name, value, cookie);
		
		response.putHeader("Set-Cookie", cookie);
		
	}


	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpAdapter#method()
	 */
	@Override
	public String method() {
		
		return request.method();
	}

	protected void handleErrorResponse(int errorCode, String message) {
		
		response.setStatusCode(errorCode);
		
		if(message != null){
			
			response.end(message);
			
		} else {
			
			response.end("Internal server error");
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpAdapter#path()
	 */
	@Override
	public String path() {
		
		return request.path();
	}

	@Override
	public String getContentType() {
		
		String[] headerParts = readHeader(CONTENT_TYPE).split(HEADER_SEP);
		
		return headerParts[0];
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpAdapter#getContentLangauge()
	 */
	@Override
	public String getContentLangauge() {
		
		String header = readHeader(CONTENT_LANGUAGE);
		
		if(header == null)
			return null;
		
		String[] headerParts = header.split(HEADER_SEP);
		
		return headerParts[0];
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpAdapter#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(String uri) {
		
		response.setStatusCode(REDIRECT_RESPONSE_CODE);
		
		response.putHeader("Location", uri);
		
		response.end();
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.HttpAdapter#createGatewayRequest()
	 */
	@Override
	public GatewayRequest createGatewayRequest(Map<String, DocumentReader> documentReaders, String tempFolder) {
		return new VertxAsynchronusRequest(this, documentReaders, tempFolder);
	}

}

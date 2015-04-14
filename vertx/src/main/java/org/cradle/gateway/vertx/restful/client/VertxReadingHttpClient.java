/**
 * 
 */
package org.cradle.gateway.vertx.restful.client;

import java.util.Map;

import org.cradle.gateway.restful.client.AsynchronousReadingHttpClient;
import org.cradle.gateway.restful.client.HttpDocumentResponseCallback;
import org.cradle.gateway.restful.client.HttpFileResponseCallback;
import org.cradle.gateway.restful.document.DocumentReader;
import org.vertx.java.core.Handler;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 27, 2014
 */
public class VertxReadingHttpClient implements AsynchronousReadingHttpClient {

	private HttpClient client;
	private Map<String, DocumentReader> documentReaders;
	private FileSystem fileSystem;
	private String fileSystemPath;
	
	public VertxReadingHttpClient(HttpClient client,
			Map<String, DocumentReader> documentReaders, String fileSystemPath, FileSystem fileSystem) {
		
		this.client = client;
		this.documentReaders = documentReaders;
		this.fileSystem = fileSystem;
		this.fileSystemPath = fileSystemPath;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.client.AsynchronousReadingHttpClient#configure(java.lang.String, int, int)
	 */
	@Override
	public void configure(String host, int port, int poolSize) {

		client = client
				.setHost(host)
				.setPort(port);

		if(poolSize == 0){

			client = client.setKeepAlive(false);

		} else {

			client = client.setMaxPoolSize(poolSize);

		}

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.client.AsynchronousReadingHttpClient#read(java.lang.String, java.util.Map, java.lang.String, org.cradle.gateway.restful.client.HttpClientResponse)
	 */
	@Override
	public void read(String uri, Map<String, String> headers,
			String documentType, Class<?> documentClass, HttpDocumentResponseCallback callback) {

		DocumentReader documentReader = documentReaders.get(documentType);

		HttpClientRequest request = getRequest(uri, documentClass, callback,
				documentReader);

		putRequestHeaders(headers, request);

		request.end();
	}

	private void putRequestHeaders(Map<String, String> headers,
			HttpClientRequest request) {
		
		String[] keys = headers.keySet().toArray(new String[]{});

		for(String key : keys)
			request.putHeader(key, headers.get(key));
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.client.AsynchronousReadingHttpClient#read(java.lang.String, java.lang.String, org.cradle.gateway.restful.client.HttpClientResponse)
	 */
	@Override
	public void read(String uri, String documentType,
			Class<?> documentClass, final HttpDocumentResponseCallback callback) {

		DocumentReader documentReader = documentReaders.get(documentType);
		
		HttpClientRequest request = getRequest(uri, documentClass, callback,
				documentReader);

		request.end();
	}

	private HttpClientRequest getRequest(String uri,
			Class<?> documentClass,
			final HttpDocumentResponseCallback callback,
			DocumentReader documentReader) {
		
		Handler<Throwable> errorHandler = new Handler<Throwable>() {

			@Override
			public void handle(Throwable e) {
				
				callback.error(e);
			}
		};
		
		client.exceptionHandler(errorHandler);
		
		HttpClientRequest request = client.get(
				uri, 
				new DocumentRequestHandler(documentReader, documentClass, callback, errorHandler)
				);
		
		return request;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.client.AsynchronousReadingHttpClient#download(java.lang.String)
	 */
	@Override
	public void download(String uri, String name, final HttpFileResponseCallback callback) {
		
		Handler<Throwable> errorHandler = new Handler<Throwable>() {

			@Override
			public void handle(Throwable e) {
				
				callback.error(e);
			}
		};
		
		client.exceptionHandler(errorHandler);
		
		client.get(
				uri, 
				new FileRequestHandler(fileSystemPath, name, callback, fileSystem, errorHandler)
				).end();
		
	}

}

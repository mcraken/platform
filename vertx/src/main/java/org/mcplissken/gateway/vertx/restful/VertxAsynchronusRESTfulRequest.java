/**
 * 
 */
package org.mcplissken.gateway.vertx.restful;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.AsynchronusRESTfulRequestHandler;
import org.mcplissken.gateway.restful.MultipartRequestHandler;
import org.mcplissken.gateway.restful.RESTfulRequest;
import org.mcplissken.gateway.restful.RESTfulResponse;
import org.mcplissken.gateway.restful.document.DocumentReader;
import org.mcplissken.gateway.restful.document.DocumentReadingExcetion;
import org.mcplissken.gateway.vertx.VertxHttpAdapter;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 12, 2014
 */
public class VertxAsynchronusRESTfulRequest extends RESTfulRequest implements Handler<Buffer>{

	private VertxHttpAdapter vertxHttpAdapter;
	
	/**
	 * @param httpAdapter
	 * @param documentReaders
	 * @param documentType
	 */
	public VertxAsynchronusRESTfulRequest(
			HttpAdapter httpAdapter,
			Map<String, DocumentReader> documentReaders, 
			String tempFolder) {

		super(httpAdapter, documentReaders, tempFolder);
		
		this.vertxHttpAdapter = ((VertxHttpAdapter) httpAdapter);
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(Buffer event) {

		byte[] input = event.getByteBuf().array();

		appendInput(input);
	}


	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.restful.RESTfulRequest#readDocumentObjectAsynchronously(org.mcplissken.osgi.restful.AsynchronusRESTfulRequestHandler)
	 */
	@Override
	public void readDocumentObjectAsynchronously(
			final AsynchronusRESTfulRequestHandler handler, final Class<?> documentType) {

		HttpServerRequest httpServerRequest = vertxHttpAdapter.getRequest();

		httpServerRequest.dataHandler(this);

		httpServerRequest.endHandler(new VoidHandler() {

			@Override
			protected void handle() {
				try {
					
					handler.handleDocument(readDocumentObject(documentType));
					
				} catch (DocumentReadingExcetion e) {
					
					e.printStackTrace();
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.restful.RESTfulRequest#readMultipartRequest(org.mcplissken.gateway.multipart.MultipartRequestHandler)
	 */
	@Override
	public void readMultipartRequest(final MultipartRequestHandler handler, final RESTfulResponse response) {
		
		final HttpServerRequest request =  vertxHttpAdapter.getRequest();
		
		final RESTfulRequest restfulRequest = this;
		
		request.response().setChunked(true);
		
		request.expectMultiPart(true);
		
		request.uploadHandler(new Handler<HttpServerFileUpload>() {

			@Override
			public void handle(HttpServerFileUpload upload) {

				String fileFullPath = createUploadName(upload.filename());

				upload.streamToFileSystem(fileFullPath);

				addUpload(fileFullPath);
			}

		});

		request.endHandler(new VoidHandler() {

			public void handle() {
				
				try {
					
					MultiMap attrs = request.formAttributes();

					Iterator<Entry<String, String>> formAttrs = attrs.iterator();

					HashMap<String, String> formAttrsMap = new HashMap<>();

					Entry<String, String> entry = null;

					while(formAttrs.hasNext()){

						entry = formAttrs.next();

						formAttrsMap.put(entry.getKey(), entry.getValue());
					}

					multipartRequestEnded(vertxHttpAdapter, restfulRequest, response, handler, formAttrsMap);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});
		
		request.exceptionHandler(new Handler<Throwable>() {

			@Override
			public void handle(Throwable e) {
				e.printStackTrace();
			}
		});
	}

}

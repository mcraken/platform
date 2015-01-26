/**
 * 
 */
package org.mcplissken.gateway.vertx.restful.client;

import java.io.File;

import org.mcplissken.gateway.restful.client.HttpFileResponseCallback;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.streams.Pump;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 12, 2014
 */
public class FileRequestHandler implements Handler<HttpClientResponse> {

	private String fullPath;
	
	private HttpFileResponseCallback fileResponseCallback;
	
	private FileSystem fileSystem;
	
	private Handler<Throwable> errorHandler;
	
	public FileRequestHandler(String path, String fileName,
			final HttpFileResponseCallback fileResponseCallback, FileSystem fileSystem, 
			Handler<Throwable> errorHandler) {
		
		initParentDir(path);
		
		fullPath = path + "/" + System.currentTimeMillis() + "_" + fileName;

		this.fileResponseCallback = fileResponseCallback;
		
		this.fileSystem = fileSystem;
			
		this.errorHandler = errorHandler;
	}

	
	private void initParentDir(String path) {
		
		File parentDir = new File(path);
		
		if (!parentDir.exists()) {
			parentDir.mkdir();
		}
	}
	

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(final HttpClientResponse response) {
		
		response.pause();
		
		fileSystem.open(fullPath, null, true, true, true, true, new AsyncResultHandler<AsyncFile>(){

			@Override
			public void handle(AsyncResult<AsyncFile> result) {
				
				final AsyncFile fileResult = result.result();
				
				Pump pump = Pump.createPump(response, fileResult);
				
				response.endHandler(new Handler<Void>() {

					@Override
					public void handle(Void event) {
						
						fileResult.close();
						fileResponseCallback.response(new File(fullPath));
					}
				});
				
				response.exceptionHandler(errorHandler);
				
				fileResult.exceptionHandler(errorHandler);
				
				pump.start();
				
				response.resume();
			}
			
		});
	}

}

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
						
						fileResult.flush(new Handler<AsyncResult<Void>>() {

							@Override
							public void handle(AsyncResult<Void> event) {
								
								fileResponseCallback.response(new File(fullPath));
							}
						});
						
						fileResult.close();
						
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

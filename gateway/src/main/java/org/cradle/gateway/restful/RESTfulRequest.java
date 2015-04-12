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
package org.cradle.gateway.restful;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.restful.document.DocumentReader;
import org.cradle.gateway.restful.document.DocumentReadingExcetion;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 6, 2014
 */
public abstract class RESTfulRequest {

	private HttpAdapter httpAdapter;

	private ByteBuffer buffer;

	private Map<String, DocumentReader> documentReaders;

	private String tempFolder;

	private ArrayList<File> uploads;

	public RESTfulRequest(HttpAdapter httpAdapter,
			Map<String, DocumentReader> documentReaders, String tempFolder) {

		this.httpAdapter = httpAdapter;
		this.documentReaders = documentReaders;
		this.tempFolder = tempFolder;
		
		this.uploads = new ArrayList<>();
		
		buffer = ByteBuffer.allocate(1024 * 1024);

	}

	public Object readDocumentObject(Class<?> documentType) throws DocumentReadingExcetion{

		String contentType = httpAdapter.getContentType();

		DocumentReader documentReader = documentReaders.get(contentType);

		return documentReader.read(documentType, buffer);

	}

	protected void appendInput(byte[] inputArr){

		buffer = ByteBuffer.allocate(inputArr.length);

		buffer.put(inputArr);

	}
	
	protected void multipartRequestEnded(HttpAdapter httpAdapter, RESTfulRequest request, RESTfulResponse response, MultipartRequestHandler handler, HashMap<String, String> formAttrsMap) throws Exception{

		Object formInstance = handler.createFormInstance();

		BeanUtils.populate(formInstance, formAttrsMap);
		
		handler.handle(httpAdapter, request, response, formInstance, uploads);

	}

	protected void addUpload(String fileFullPath) {

		uploads.add(new File(fileFullPath));
	}

	protected String createUploadName(String fileName) {

		return tempFolder + "/" + System.currentTimeMillis() + "_" + fileName;
	}

	public abstract void readDocumentObjectAsynchronously(AsynchronusRESTfulRequestHandler handler, Class<?> documentType);
	
	public abstract void readMultipartRequest(MultipartRequestHandler handler, RESTfulResponse response);
}

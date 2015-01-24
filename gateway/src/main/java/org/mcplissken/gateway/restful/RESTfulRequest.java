/**
 * 
 */
package org.mcplissken.gateway.restful;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.document.DocumentReader;
import org.mcplissken.gateway.restful.document.DocumentReadingExcetion;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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

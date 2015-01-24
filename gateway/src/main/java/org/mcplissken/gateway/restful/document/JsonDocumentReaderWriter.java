/**
 * 
 */
package org.mcplissken.gateway.restful.document;

import java.nio.ByteBuffer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 11, 2014
 */
public class JsonDocumentReaderWriter implements DocumentWriter, DocumentReader {

	private Gson gson;

	public void init(){

		gson = new GsonBuilder().create();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.osgi.vertx.restful.document.DocumentWriter#write(java.lang.Object)
	 */
	@Override
	public void write(Object document, ByteBuffer output) {

		output.put(gson.toJson(document).getBytes());
	}

	/* (non-Javadoc)
	 * @see com.mubasher.osgi.vertx.restful.document.DocumentReader#readDocument(java.lang.Class, java.io.Reader)
	 */
	@Override
	public Object read(Class<?> documentType, ByteBuffer input) {

		String inputStr = null;

		byte[] byteBuff = unpackBuffer(input);

		inputStr = new String(byteBuff);

		return gson.fromJson(inputStr, documentType);

	}

	private byte[] unpackBuffer(ByteBuffer input) {

		input.flip();

		int length = input.limit();

		byte[] bytesBuff = new byte[length];

		input.get(bytesBuff);

		input.clear();

		return bytesBuff;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.document.DocumentWriter#write(java.lang.Object, java.lang.StringBuffer)
	 */
	@Override
	public void write(Object document, StringBuffer buffer) {

		buffer.append(gson.toJson(document));
	}

}
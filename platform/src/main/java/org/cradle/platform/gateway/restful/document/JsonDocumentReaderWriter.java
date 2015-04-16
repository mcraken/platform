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
package org.cradle.platform.gateway.restful.document;

import java.nio.ByteBuffer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 11, 2014
 */
public class JsonDocumentReaderWriter implements DocumentWriter, DocumentReader {

	private Gson gson;

	public void init(){

		gson = new GsonBuilder().create();
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.restful.document.DocumentWriter#write(java.lang.Object)
	 */
	@Override
	public void write(Object document, ByteBuffer output) {

		output.put(gson.toJson(document).getBytes());
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.restful.document.DocumentReader#readDocument(java.lang.Class, java.io.Reader)
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
	 * @see org.cradle.gateway.restful.document.DocumentWriter#write(java.lang.Object, java.lang.StringBuffer)
	 */
	@Override
	public void write(Object document, StringBuffer buffer) {

		buffer.append(gson.toJson(document));
	}

}
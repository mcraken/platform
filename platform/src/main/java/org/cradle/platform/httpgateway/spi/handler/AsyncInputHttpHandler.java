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
package org.cradle.platform.httpgateway.spi.handler;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.cradle.platform.httpgateway.spi.GatewayResponse;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 21, 2015
 */
public abstract class AsyncInputHttpHandler extends BasicHttpHandler {

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.BasicHttpHandler#service(org.cradle.platform.httpgateway.HttpAdapter, org.cradle.platform.httpgateway.spi.GatewayRequest, org.cradle.platform.httpgateway.spi.GatewayResponse)
	 */
	@Override
	public void service(final HttpAdapter httpAdapter, GatewayRequest request,
			GatewayResponse response) throws BadRequestException,
			UnauthorizedException {



		request.readDocumentObjectAsynchronously(new AsynchronusRequestHandler() {

			@Override
			public void handleDocument(Object document) {
				
				try {
					
					execute(httpAdapter, document);
					
				} catch (HttpException e) {

					httpAdapter.exception(e);
				}
			}
		}, getDocumentType());




	}

	protected abstract Class<?> getDocumentType();

	protected abstract void execute(HttpAdapter httpAdapter, Object document) throws HttpException;
}

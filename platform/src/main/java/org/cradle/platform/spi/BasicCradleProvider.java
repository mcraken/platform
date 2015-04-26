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
package org.cradle.platform.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public abstract class BasicCradleProvider implements CradleProvider {

	private RegistrationPrincipal principalChain;
	private RegistrationAgent registrationAgent;
	
	public BasicCradleProvider(RegistrationPrincipal principalChain) {
		
		this.principalChain = principalChain;
		
		registrationAgent = new RegistrationAgent() {
			
			@Override
			public <T>void register(Annotation annotation,
					T httpHandler) {
				
				registerHandler(annotation, httpHandler);
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpGateway#registerHandler(java.lang.Object)
	 */
	@Override
	public <T>void registerController(T handler) {

		checkNotNull(handler);
		
		for (Method method : handler.getClass().getMethods())
		{
			principalChain.execute(registrationAgent, handler, method);
		}
	}
	
	protected abstract <T>void registerHandler(Annotation annotation, T handler);

}

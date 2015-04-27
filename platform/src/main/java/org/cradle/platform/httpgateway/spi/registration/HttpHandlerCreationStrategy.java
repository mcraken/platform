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
package org.cradle.platform.httpgateway.spi.registration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 27, 2015
 */
public interface HttpHandlerCreationStrategy {
	
	public BasicHttpHandler createHttpHandler(final Object handler, final Method target, final HttpMethod annotation);
	
	public void isMethodValid(Method target, Annotation annotation);
}

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
package org.cradle.platform.httpgateway.spi;

import java.lang.reflect.Method;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public abstract class RegistrationPrincipal {
	
	private RegistrationPrincipal next;

	/**
	 * @param next
	 */
	public RegistrationPrincipal(RegistrationPrincipal next) {
		this.next = next;
	}
	
	public void execute(RegistrationAgent agent, Object handler, Method target){
		executePrincipal(agent, handler, target);
		
		if(next != null)
			next.execute(agent, handler, target);
	}

	/**
	 * 
	 */
	protected abstract void executePrincipal(RegistrationAgent agent, Object handler, Method target);
}

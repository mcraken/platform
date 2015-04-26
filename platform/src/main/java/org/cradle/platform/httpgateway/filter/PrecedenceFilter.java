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
package org.cradle.platform.httpgateway.filter;


/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 26, 2015
 */
public abstract class PrecedenceFilter implements Filter {

	private int precedence;
	
	/**
	 * @param precedence
	 */
	public PrecedenceFilter(int precedence) {
		this.precedence = precedence;
	}

	public int compare(PrecedenceFilter filter){
		
		if(precedence < filter.precedence){
			return -1;
		} else if(precedence == filter.precedence){
			return 0;
		} else {
			return 1;
		}
	}

}

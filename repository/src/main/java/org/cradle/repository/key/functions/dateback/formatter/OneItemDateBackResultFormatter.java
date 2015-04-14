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
package org.cradle.repository.key.functions.dateback.formatter;

import org.cradle.datetime.DateTimeOperation;
import org.cradle.datetime.dateback.DateBackStrategy;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 17, 2014
 */
public class OneItemDateBackResultFormatter implements DateBackResultFormatter {

	/* (non-Javadoc)
	 * @see org.cradle.repository.key.functions.dateback.DateBackResult#createResult()
	 */
	@Override
	public String createResult(DateBackStrategy strategy, int amount, DateTimeOperation start) {

		StringBuilder result = new StringBuilder();

		result.append(strategy.back(start, amount).getMilliseconds());
		
		return result.toString();
	}
	
}

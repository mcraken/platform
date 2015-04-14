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
package org.cradle.datetime.dateback;

import org.cradle.datetime.DateTimeOperation;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 17, 2014
 */
public class WindowDateBackStrategy implements DateBackStrategy {

	private int minutes;
	
	/**
	 * 
	 */
	public WindowDateBackStrategy() {
	}
	
	/**
	 * 
	 */
	public WindowDateBackStrategy(int minutes) {
		
		this.minutes = minutes;
	}
	
	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	@Override
	public DateTimeOperation back(DateTimeOperation start, int amount) {

		return start.minusWindow(minutes, amount);
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.key.functions.dateback.strategy.DateBackStrategy#shift(org.cradle.datetime.DateTimeOperation)
	 */
	@Override
	public DateTimeOperation shift(DateTimeOperation start) {

		return start.startOfWindow(minutes);
	}

}
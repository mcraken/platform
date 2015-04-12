/**
 * 
 */
package org.cradle.datetime.dateback;

import org.cradle.datetime.DateTimeOperation;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 17, 2014
 */
public class YearsDateBackStrategy implements DateBackStrategy {

	/* (non-Javadoc)
	 * @see org.cradle.repository.key.functions.dateback.DateBackStrategy#substractDate(org.joda.time.DateTime, int)
	 */
	@Override
	public DateTimeOperation back(DateTimeOperation start, int amount) {
		
		return start.minusYears(amount);
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.key.functions.dateback.strategy.DateBackStrategy#shift(org.cradle.datetime.DateTimeOperation)
	 */
	@Override
	public DateTimeOperation shift(DateTimeOperation start) {
		
		return start.startOfYear().startOfDay().startOfHour().startOfMinute().startOfSecond();
	}

}
/**
 * 
 */
package org.mcplissken.datetime.dateback;

import org.mcplissken.datetime.DateTimeOperation;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 17, 2014
 */
public class HoursDateBackStrategy implements DateBackStrategy {

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.key.functions.dateback.DateBackStrategy#substractDate(org.joda.time.DateTime, int)
	 */
	@Override
	public DateTimeOperation back(DateTimeOperation start, int amount) {
		
		return start.minusHours(amount);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.key.functions.dateback.strategy.DateBackStrategy#shift(org.joda.time.DateTime)
	 */
	@Override
	public DateTimeOperation shift(DateTimeOperation start) {
		
		return start.startOfHour().startOfMinute().startOfSecond();
	}

}

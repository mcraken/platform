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
	 * @see com.mubasher.market.repository.key.functions.dateback.strategy.DateBackStrategy#shift(com.mubasher.datetime.DateTimeOperation)
	 */
	@Override
	public DateTimeOperation shift(DateTimeOperation start) {

		return start.startOfWindow(minutes);
	}

}
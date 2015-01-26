/**
 * 
 */
package org.mcplissken.repository.key.functions.dateback.formatter;

import org.mcplissken.datetime.DateTimeOperation;
import org.mcplissken.datetime.dateback.DateBackStrategy;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 17, 2014
 */
public class ListDateBackResultFormatter implements DateBackResultFormatter{

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.key.functions.dateback.DateBackResult#createResult()
	 */
	@Override
	public String createResult(DateBackStrategy strategy, int amount, DateTimeOperation start) {

		StringBuilder result = new StringBuilder();

		amount = appendElement(amount, start, result);
		
		while(amount >= 0){
			
			result.append(",");
			
			start = strategy.back(start, 1);
			
			amount = appendElement(amount, start, result);
		}

		return result.toString();
	}

	private int appendElement(int amount, DateTimeOperation start, StringBuilder result) {
		
		amount--;
		
		result.append(start.getMilliseconds());
		
		return amount;
	}
}

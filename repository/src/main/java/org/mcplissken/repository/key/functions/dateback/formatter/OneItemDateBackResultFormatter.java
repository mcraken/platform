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
public class OneItemDateBackResultFormatter implements DateBackResultFormatter {

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.key.functions.dateback.DateBackResult#createResult()
	 */
	@Override
	public String createResult(DateBackStrategy strategy, int amount, DateTimeOperation start) {

		StringBuilder result = new StringBuilder();

		result.append(strategy.back(start, amount).getMilliseconds());
		
		return result.toString();
	}
	
}

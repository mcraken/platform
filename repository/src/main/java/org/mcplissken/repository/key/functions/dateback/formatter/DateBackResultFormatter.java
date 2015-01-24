/**
 * 
 */
package org.mcplissken.repository.key.functions.dateback.formatter;

import org.mcplissken.datetime.DateTimeOperation;
import org.mcplissken.datetime.dateback.DateBackStrategy;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 17, 2014
 */
public interface DateBackResultFormatter {
	
	public static final String DAYS = "days";
	public static final String WEEKS = "weeks";
	public static final String MONTHS = "months";
	public static final String YEARS = "years";
	public static final String ZERO_CLOCK = "000000";
	public static final String DATE_FORMAT = "yyyyMMdd";
	public static final String DATE_TIME_FORMAT = "yyyyMMdd HHmmss";
	
	public String createResult(DateBackStrategy strategy, int amount, DateTimeOperation start);
}

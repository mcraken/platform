/**
 * 
 */
package org.cradle.repository.key.functions.dateback.formatter;

import org.cradle.datetime.DateTimeOperation;
import org.cradle.datetime.dateback.DateBackStrategy;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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

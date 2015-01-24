/**
 * 
 */
package org.mcplissken.repository.key.functions.dateback;

import java.util.Map;

import org.mcplissken.datetime.DateTimeOperation;
import org.mcplissken.datetime.dateback.DateBackStrategy;
import org.mcplissken.repository.StructuredRepository;
import org.mcplissken.repository.key.exception.ServerFunctionException;
import org.mcplissken.repository.key.functions.BasicServerFunction;
import org.mcplissken.repository.key.functions.ServerFunctionHandler;
import org.mcplissken.repository.key.functions.dateback.formatter.DateBackResultFormatter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 9, 2014
 */
public class DateBack extends BasicServerFunction implements ServerFunctionHandler {

	private Map<String, DateBackResultFormatter> resultFormatters;
	private Map<String, DateBackStrategy> dateStrategies;

	/**
	 * @param dateStrategies the dateStrategies to set
	 */
	public void setDateStrategies(Map<String, DateBackStrategy> dateStrategies) {
		this.dateStrategies = dateStrategies;
	}

	/**
	 * @param resultFormatters the resultFormatters to set
	 */
	public void setResultFormatters(
			Map<String, DateBackResultFormatter> resultFormatters) {

		this.resultFormatters = resultFormatters;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.key.functions.ServerFunctionHandler#handle(com.mubasher.market.repository.ModelRepository, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected String resolveFunction(
			StructuredRepository repository, 
			String resourceName,
			String propertyName, 
			String parameters)
					throws ServerFunctionException {

		String [] params = parameters.split(",");

		String formatterType = params[0];

		String strategyType = params[1];

		int numberOfHops = Integer.parseInt(params[2]);

		DateTimeOperation start = new DateTimeOperation();

		DateBackStrategy strategy = getStrategy(strategyType, start);

		DateBackResultFormatter formatter = resultFormatters.get(formatterType);

		return formatter.createResult(strategy, numberOfHops, start) ;

	}

	private DateBackStrategy getStrategy(String strategyType,
			DateTimeOperation start) {

		DateBackStrategy strategy = dateStrategies.get(strategyType);

		strategy.shift(start);

		return strategy;
	}

}

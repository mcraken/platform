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
package org.cradle.repository.key.functions.dateback;

import java.util.Map;

import org.cradle.datetime.DateTimeOperation;
import org.cradle.datetime.dateback.DateBackStrategy;
import org.cradle.repository.StructuredRepository;
import org.cradle.repository.key.exception.ServerFunctionException;
import org.cradle.repository.key.functions.BasicServerFunction;
import org.cradle.repository.key.functions.ServerFunctionHandler;
import org.cradle.repository.key.functions.dateback.formatter.DateBackResultFormatter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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
	 * @see org.cradle.repository.key.functions.ServerFunctionHandler#handle(org.cradle.repository.ModelRepository, java.lang.String, java.lang.String, java.lang.String)
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

/**
 * 
 */
package org.mcplissken.cache.ehcache.criteriahandlers;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;

import net.sf.ehcache.search.expression.And;
import net.sf.ehcache.search.expression.Criteria;
import net.sf.ehcache.search.expression.Or;

import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 7, 2014
 */
public class LogicalCriteria extends BasicCriteriaHandler {

	@Override
	protected Criteria resolveCriteria(RestCriteria criteria,
			Map<String, Criteria> criterias) throws Exception {
		
		Stack<String> oprands = new Stack<>();
		
		oprands.addAll(Arrays.asList(criteria.readCriteriaNames()));
		
		String logicalOperator = criteria.getValue();
		
		if(logicalOperator.equalsIgnoreCase("and"))
			return and(oprands, criterias);
		
		if(logicalOperator.equalsIgnoreCase("or"))
			return or(oprands, criterias);
		
		throw new InvalidCriteriaException(logicalOperator);
	}
	
	private Criteria or(Stack<String> oprands, Map<String, Criteria> criterias) {
		
		if(oprands.size() == 2)
			return new Or(popOperand(oprands, criterias), popOperand(oprands, criterias));
		
		return new Or(popOperand(oprands, criterias), or(oprands, criterias));
	}

	private Criteria popOperand(Stack<String> oprands,
			Map<String, Criteria> criterias) {
		
		String operandId = oprands.pop();
		
		Criteria operand = criterias.get(operandId);
		
		criterias.remove(operandId);
		
		return operand;
	}
	
	private Criteria and(Stack<String> oprands,
			Map<String, Criteria> criterias) {
		
		if(oprands.size() == 2)
			return new And(popOperand(oprands, criterias), popOperand(oprands, criterias));
		
		return new And(popOperand(oprands, criterias), and(oprands, criterias));
	}

}
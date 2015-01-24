/**
 * 
 */
package org.mcplissken.repository.key;

/**
 * @author Raken
 *
 */
public class RestProjection {
	
	public String propertyName;
	
	public String function;
	
	public RestProjection(String projection){
		readFunction(projection);
		readPropertyName(projection);
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public String getFunction() {
		return function;
	}
	
	private void readFunction(String projection){
		
		int openBracketPlace = projection.indexOf('(');
	
		function = projection.substring(0, openBracketPlace).toLowerCase();
	}
	
	private void readPropertyName(String projection){
		
		int openBracketPlace = projection.indexOf('(');
		
		int closeBracketPlace = projection.indexOf(')');
		
		propertyName = projection.substring(openBracketPlace + 1, closeBracketPlace);
	}
	
}

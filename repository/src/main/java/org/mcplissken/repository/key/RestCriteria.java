/**
 * 
 */
package org.mcplissken.repository.key;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mcplissken.repository.key.exception.BadCriteriaValueException;
import org.mcplissken.repository.key.exception.InvalidCriteriaSyntaxException;

/**
 * @author sherif.shawky
 * Search criteria to be used by the composite rest model
 */
public class RestCriteria {


	private final static String INTEGER_PATTERN = "^-?\\d+";
	private final static String DOUBLE_Pattern = "^-?\\d+.\\d+";
	private final static String DATE_PATTERN = "^(?=\\d)(?:(?!(?:(?:0?[5-9]|1[0-4])(?:\\.|-|\\/)10(?:\\.|-|\\/)(?:1582))|(?:(?:0?[3-9]|1[0-3])(?:\\.|-|\\/)0?9(?:\\.|-|\\/)(?:1752)))(31(?!(?:\\.|-|\\/)(?:0?[2469]|11))|30(?!(?:\\.|-|\\/)0?2)|(?:29(?:(?!(?:\\.|-|\\/)0?2(?:\\.|-|\\/))|(?=\\D0?2\\D(?:(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:(?:\\d\\d)(?:[02468][048]|[13579][26])(?!\\x20BC))|(?:00(?:42|3[0369]|2[147]|1[258]|09)\\x20BC))))))|2[0-8]|1\\d|0?[1-9])([-.\\/])(1[012]|(?:0?[1-9]))\\2((?=(?:00(?:4[0-5]|[0-3]?\\d)\\x20BC)|(?:\\d{4}(?:$|(?=\\x20\\d)\\x20)))\\d{4}(?:\\x20BC)?)(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$";
	private static final SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss a");
	
	private String propertyName;
	private String value;
	private boolean valueEncoded;
	private boolean dontForceType;
	private boolean serverFunction;
	
	private int underScrorePlace;
	private int openBracketPlace ;
	private int closeBracketPlace;
	
	private String serverFunctionValue;

	public RestCriteria(){

	}

	public RestCriteria(String propertyName, String value){
		this.propertyName = propertyName;
		this.value = value;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public String[] readCriteriaNames(){
		return readCriteriaName().split(",");
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getCrudeValue(){
		return value;
	}
	
	public String getValue() throws BadCriteriaValueException{

		if (isValueEncoded())
			return convertUnicodeValueIntoString();
		
		if(isServerFunction())
			return this.serverFunctionValue;
		
		return this.value;
	}

	public boolean isValueEncoded() {
		return this.valueEncoded;
	}
	
	private String convertUnicodeValueIntoString() {
		
		StringTokenizer localStringTokenizer = new StringTokenizer(isServerFunction()?this.serverFunctionValue:this.value, "u");
		
		String str = "";  
		
		while(localStringTokenizer.hasMoreTokens()){
			str = str + (char)Integer.parseInt(localStringTokenizer.nextToken(), 16);	
		}

		return str;
	}
	
	public void setValueEncoded(boolean paramBoolean) {
		this.valueEncoded = paramBoolean;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isDontForceType() {
		return dontForceType;
	}

	public void setDontForceType(boolean dontForceType) {
		this.dontForceType = dontForceType;
	}
	
	public boolean isServerFunction() {
		return serverFunction;
	}

	public void setServerFunction(boolean serverFunction) {
		this.serverFunction = serverFunction;
	}
	
	public void parse() throws Exception{
		
		underScrorePlace = propertyName.indexOf('_');
		openBracketPlace = propertyName.indexOf('(');
		closeBracketPlace = propertyName.indexOf(')');

		if(underScrorePlace == -1 || openBracketPlace == -1 || closeBracketPlace == -1)
			throw new InvalidCriteriaSyntaxException(new IndexOutOfBoundsException());
	}
	
	public String readServerFunctionName(){
		return value.substring(0, value.indexOf("("));
	}
	
	public String readFunctionParameters(){
		return value.substring(value.indexOf("(") + 1, value.indexOf(")"));
	}
	
	public String readCriteriaId() {

		return propertyName.substring(0, underScrorePlace);
	}

	public String readCriteriaFunction(){

		return propertyName.substring(underScrorePlace + 1, openBracketPlace);
	}

	public String readCriteriaName(){

		return propertyName.substring(openBracketPlace + 1, closeBracketPlace);
	}

	public Object getParsedValue() throws BadCriteriaValueException{
		return getParsedValue(getValue());
	}
	
	public Object[] getParsedValues() throws BadCriteriaValueException{
		
		String valuesAsString = getValue();
		
		String[] valuesAsArray = valuesAsString.split(",");
		
		Object[] values = new Object[valuesAsArray.length];
		
		for(int i = 0; i < valuesAsArray.length; i++){
			
			values[i] = getParsedValue(valuesAsArray[i]);
		}
		
		return values;
			
	}
	
	private Object getParsedValue(String encodedValue) throws BadCriteriaValueException{

		
		if(!dontForceType){
			
			if(encodedValue.matches(INTEGER_PATTERN)){
				
				try{
					
					return Integer.parseInt(encodedValue);
					
				} catch(NumberFormatException e){
					
					return Long.parseLong(encodedValue);
				}
			}

			else if(encodedValue.matches(DOUBLE_Pattern))
				return Double.parseDouble(encodedValue);

			else if(encodedValue.matches(DATE_PATTERN)){
				
				try {
					
					return gmtDateFormat.parse(encodedValue);
					
				} catch (ParseException e) {
					
					throw new BadCriteriaValueException(encodedValue);
				}
			}
		}

		return encodedValue;

	}

	public String getServerFunctionValue() {
		return serverFunctionValue;
	}
	
	public void setServerFunctionValue(String result) {
		this.serverFunctionValue = result;
	}
	
	public boolean propertyNameEquals(String name){
		
		if(name.equals(readCriteriaName()))
			return true;
		
		return false;
				
	}
	
	@Override
	public int hashCode() {
		
		return new HashCodeBuilder()
		.append(propertyName)
		.append(value)
		.append(serverFunction)
		.append(dontForceType)
		.append(valueEncoded).
		toHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		RestCriteria other = (RestCriteria) obj;
		
		if(this == other)
			return true;
		
		return new EqualsBuilder()
		.append(this.propertyName, other.propertyName)
		.append(this.value, other.value)
		.append(this.serverFunction, other.serverFunction)
		.append(this.dontForceType, other.dontForceType)
		.append(this.valueEncoded, other.valueEncoded)
		.isEquals();
	}
}

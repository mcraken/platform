/**
 * 
 */
package org.mcplissken.gateway.restful;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 21, 2015
 */
public class ResponseObject {
	
	private Object response;
	
	private List<String> messagesIds;
	
	private List<String> errorsIds;
	
	private String contentType;

	/**
	 * 
	 */
	public ResponseObject() {
	}
	
	public ResponseObject(Object response) {
		this.response = response;
	}
	
	public ResponseObject(List<String> errorsIds, String contentType) {
		this.errorsIds = errorsIds;
		this.contentType = contentType;
	}

	public ResponseObject(Object response, String contentType) {
		super();
		this.response = response;
		this.contentType = contentType;
	}

	public void addMessageId(String messageId){
		
		if(messagesIds == null)
			messagesIds = new ArrayList<>();
		
		messagesIds.add(messageId);
	}
	
	public void addErrorId(String errorId){
		
		if(errorsIds == null)
			errorsIds = new ArrayList<>();
		
		errorsIds.add(errorId);
		
	}
	
	public void setResponse(String response){
		this.response = response;
	}
	
	/**
	 * @return the response
	 */
	public Object getResponse() {
		return response;
	}
	
	/**
	 * @return the errorsIds
	 */
	public List<String> getErrorsIds() {
		return errorsIds;
	}
	
	/**
	 * @return the messagesIds
	 */
	public List<String> getMessagesIds() {
		return messagesIds;
	}
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
}

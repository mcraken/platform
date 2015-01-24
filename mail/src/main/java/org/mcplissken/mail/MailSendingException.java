/**
 * 
 */
package org.mcplissken.mail;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 28, 2014
 */
public class MailSendingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MailSendingException(Throwable e){
		super("Error occured while sending mail.", e);
	}

}

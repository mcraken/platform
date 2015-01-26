/**
 * 
 */
package org.mcplissken.mail;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 28, 2014
 */
public interface MailService {
	
	public void sendMail(String to, String subject, String body) throws MailSendingException;
	
}

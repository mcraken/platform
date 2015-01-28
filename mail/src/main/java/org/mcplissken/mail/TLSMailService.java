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
package org.mcplissken.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 28, 2014
 */
public class TLSMailService implements MailService {

	private String userName;
	private String password;
	private String fromAddress;

	private Properties properties;

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param fromAddress the fromAddress to set
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void init(){

//		try {
//			
//			System.out.println("Sending...");
//
//			sendMail("mcrakens@gmail.com", "OSGi Test", "Hello from OSGi!");
//			
//			System.out.println("Done");
//
//		} catch (MailSendingException e) {
//
//			e.printStackTrace();
//		}

	}


	private Session createSession(){

		return Session.getInstance(
				properties,
				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						
						return new PasswordAuthentication(userName, password);
					}
				});
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.broadcast.mail.MailService#sendMail(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMail(String to, String subject, String body) throws MailSendingException{

		Message message = new MimeMessage(createSession());

		try {

			message.setFrom(new InternetAddress(fromAddress));

			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.setSubject(subject);

			message.setText(body);

			Transport.send(message);

		} catch (Exception e) {

			throw new MailSendingException(e);
		} 


	}

}

/**
 * 
 */
package org.cradle.reporting.mail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.cradle.mail.MailSendingException;
import org.cradle.mail.MailService;
import org.cradle.reporting.SystemReportingService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 7, 2014
 */
public class TLSMailSystemReportingService implements SystemReportingService {

	private MailService mailService;
	private String to;

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		
		this.to = to;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		
		this.mailService = mailService;
	}

	/* (non-Javadoc)
	 * @see org.cradle.system.reporting.SystemReportingService#info(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void info(String reporter, String channel, String message) {

		try {

			mailService.sendMail(to, "Info from server: " + reporter, message);

		} catch (MailSendingException e) {

			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.system.reporting.SystemReportingService#exception(java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void exception(String reporter, String channel, Throwable ex) {


		try {
			
			StringWriter writer = new StringWriter();

			ex.printStackTrace(new PrintWriter(writer));

			writer.flush();

			String error = writer.getBuffer().toString();
			
			mailService.sendMail(to, "Error from server: " + reporter, error);

		} catch (MailSendingException e) {

			e.printStackTrace();
		}
	}

}

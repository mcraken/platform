/**
 * 
 */
package org.mcplissken.security.shiro;

import java.io.Serializable;

import org.apache.shiro.session.mgt.SessionKey;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Dec 24, 2014
 */
public class SimpleSessionKey implements SessionKey {
	
	private String sessionId;
	
	public SimpleSessionKey(String sessionId) {
		this.sessionId = sessionId;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.session.mgt.SessionKey#getSessionId()
	 */
	@Override
	public Serializable getSessionId() {
		return sessionId;
	}

}

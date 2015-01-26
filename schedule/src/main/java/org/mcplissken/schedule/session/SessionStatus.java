/**
 * 
 */
package org.mcplissken.schedule.session;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 12, 2014
 */
public class SessionStatus {
	
	public static final String IN_SESSION = "insession";
	public static final String OUT_SESSION = "outsession";
	public static final String START_SESSION = "startsession";
	public static final String END_SESSION = "endsession";
	
	private String type;
	
	public SessionStatus(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	public void apply(SessionStatusCallback sessionCallback){
		
		switch (type){
		
			case IN_SESSION:
				sessionCallback.inSession();
				break;
				
			case OUT_SESSION:
				sessionCallback.outSession();
				break;
				
			case START_SESSION:
				sessionCallback.startSession();
				break;
				
			case END_SESSION:
				sessionCallback.endSession();
				break;
		}
		
	}
}

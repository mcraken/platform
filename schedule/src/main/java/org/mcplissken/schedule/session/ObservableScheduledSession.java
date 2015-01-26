/**
 * 
 */
package org.mcplissken.schedule.session;

import org.mcplissken.disruptor.DisruptorOperation;
import org.mcplissken.reporting.SystemReportingService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 12, 2014
 */
public class ObservableScheduledSession extends ScheduledSession {

	private DisruptorOperation<SessionStatus> sessionOperation;

	private SessionObserver[] observers;
	
	/**
	 * @param observers the observers to set
	 */
	public void setObservers(SessionObserver[] observers) {
		this.observers = observers;
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.session.ScheduledSession#initSession()
	 */
	@Override
	protected void initSession() throws SessionException {
		
		for(SessionObserver observer : observers)
			observer.init();
		
		sessionOperation = new DisruptorOperation<>(1024, DisruptorOperation.BLOCKING, observers);
		
		sessionOperation.start();
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.session.ScheduledSession#startSession()
	 */
	@Override
	protected void startSession() throws SessionException {

		sessionOperation.onData(new SessionStatus(SessionStatus.START_SESSION));
		
		reportingService.info(getSessionName(), SystemReportingService.FILE, "Session " + getSessionName() + " started");

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.session.ScheduledSession#inSession()
	 */
	@Override
	protected void inSession() throws SessionException {

		sessionOperation.onData(new SessionStatus(SessionStatus.IN_SESSION));
		
		reportingService.info(getSessionName(), SystemReportingService.FILE, "Scheduler of " + getSessionName() + " is started after session start time and before session end time");

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.session.ScheduledSession#outSession()
	 */
	@Override
	protected void outSession() throws SessionException {
		
		sessionOperation.onData(new SessionStatus(SessionStatus.OUT_SESSION));
		
		reportingService.info(getSessionName(), SystemReportingService.FILE, "Scheduler of " + getSessionName() + " is started after session end time");
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.session.ScheduledSession#endSession()
	 */
	@Override
	protected void endSession() throws SessionException {
		
		sessionOperation.onData(new SessionStatus(SessionStatus.END_SESSION));
		
		reportingService.info(getSessionName(), SystemReportingService.FILE, "Session " + getSessionName() + " ended");
	}

}

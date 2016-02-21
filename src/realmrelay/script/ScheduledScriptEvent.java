package realmrelay.script;

import realmrelay.PacketManager;
import realmrelay.User;


public class ScheduledScriptEvent extends ScriptEvent {
	
	private final long fireTime;
	private final String eventMethod;
	private final Object[] objects;

	public ScheduledScriptEvent(User user, double seconds, String eventMethod, Object[] objects, PacketManager pm) {
		super(user, pm);
		if (seconds < 0) {
			throw new IllegalArgumentException("seconds must be >= 0");
		}
		this.fireTime = System.currentTimeMillis() + (long)(seconds * 1000);
		this.eventMethod = eventMethod;
		this.objects = objects;
	}
	
	public String getEventMethod() {
		return this.eventMethod;
	}
	
	public Object[] getArguments() {
		Object[] objects = new Object[this.objects.length];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = this.objects[i];
		}
		return objects;
	}
	
	public boolean isExpired() {
		return System.currentTimeMillis() >= this.fireTime;
	}

}

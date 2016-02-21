package realmrelay.script;

import realmrelay.PacketManager;
import realmrelay.User;
import realmrelay.packets.Packet;


public class PacketScriptEvent extends ScriptEvent {
	
	private final Packet packet;
	
	private boolean bCancelled = false;

	protected PacketScriptEvent(User user, Packet packet, PacketManager pm) {
		super(user, pm);
		this.packet = packet;
	}
	
	public void cancel() {
		this.bCancelled = true;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
	
	public boolean isCancelled() {
		return this.bCancelled;
	}

}

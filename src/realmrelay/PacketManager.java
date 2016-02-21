package realmrelay;

import realmrelay.data.PlayerData;
import realmrelay.data.StatData;
import realmrelay.data.Status;
import realmrelay.packets.Packet;
import realmrelay.packets.server.Create_SuccessPacket;
import realmrelay.packets.server.New_TickPacket;
import realmrelay.script.PacketScriptEvent;

public class PacketManager {

	public static PlayerData playerData = new PlayerData();
	
	public static void onServerPacketEvent(PacketScriptEvent event) throws Exception {
		Packet packet = event.getPacket();
		if (packet instanceof Create_SuccessPacket) {
			Create_SuccessPacket csp = (Create_SuccessPacket) packet;
			//echo("My ID : " + csp.objectId);
			playerData.id = csp.objectId;
		} else if (packet instanceof New_TickPacket) {
			New_TickPacket ent = (New_TickPacket) packet;
			for (Status he : ent.statuses) {
				for (StatData data : he.data) {
					if (he.objectId == playerData.id) {
						playerData.parseNewTICK(data.id, data.intValue, data.stringValue);
						playerData.pos = he.pos;
					}
				}
			}
		}
	}
}

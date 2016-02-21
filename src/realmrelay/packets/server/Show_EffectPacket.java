package realmrelay.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import realmrelay.data.Location;
import realmrelay.packets.Packet;



public class Show_EffectPacket extends Packet {
	
	public int effectType;
	public int targetObjectId;
	public Location pos1 = new Location();
	public Location pos2 = new Location();
	public int color;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.effectType = in.readUnsignedByte();
		this.targetObjectId = in.readInt();
		this.pos1.parseFromInput(in);
		this.pos2.parseFromInput(in);
		this.color = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(this.effectType);
		out.writeInt(this.targetObjectId);
		this.pos1.writeToOutput(out);
		this.pos2.writeToOutput(out);
		out.writeInt(this.color);
	}

}

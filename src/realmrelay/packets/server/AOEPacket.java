package realmrelay.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import realmrelay.data.Location;
import realmrelay.packets.Packet;



public class AOEPacket extends Packet {
	
	public Location pos = new Location();
	public float radius;
	public int damage;
	public int effect;
	public float duration;
	public int origType;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.pos.parseFromInput(in);
		this.radius = in.readFloat();
		this.damage = in.readUnsignedShort();
		this.effect = in.readUnsignedByte();
		this.duration = in.readFloat();
		this.origType = in.readUnsignedShort();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		this.pos.writeToOutput(out);
		out.writeFloat(this.radius);
		out.writeShort(this.damage);
		out.writeByte(this.effect);
		out.writeFloat(this.duration);
		out.writeShort(this.origType);
	}

}

var ID_PLAYERTEXT = $.findPacketId("PLAYERTEXT");
var ID_CREATE_SUCCESS = $.findPacketId("CREATE_SUCCESS");
var ID_NEW_TICK = $.findPacketId("NEW_TICK");
var ID_MOVE = $.findPacketId("MOVE");

var pos;
var newticks = 0;

function onServerPacket(event) {
	var packet = event.getPacket();
	switch (packet.id()) {
		case ID_CREATE_SUCCESS: {
			//start spam
			//spam();
		}
		case ID_NEW_TICK: {
			if(!pos)
				pos = $.getPlayerData().pos;
			else if(pos.x == 0 && pos.y == 0)
				pos = $.getPlayerData().pos;
			var dx = 142-pos.x;
			var dy = 132-pos.y;
			var tickTime = packet.tickTime?packet.tickTime:0;
			var dratio = Math.min(1, (4+5.6*(10/75))/1000*tickTime/Math.sqrt(dx*dx+dy*dy));
			dx *= dratio;
			dy *= dratio;
			if(newticks < 10)
			{
				newticks++;
			}
			else
			{
				//pos.x += dx;
				//pos.y += dy;
				//$.getPlayerData().pos += 0.1;
			}
			$.echo($.getPlayerData().pos.x);
			$.echo($.getPlayerData().pos.y);
			var movePacket = $.createPacket(ID_MOVE);
			movePacket.tickId = packet.tickId;
			$.echo("tickId: "+packet.tickId);
			movePacket.time = $.getTime();
			movePacket.newPosition = $.getPlayerData().pos;
			//$.sendToServer(movePacket);
			break;
		}
	}
}

function spam(event) {
	var playerTextPacket = $.createPacket(ID_PLAYERTEXT);
	playerTextPacket.text = "I am a"
	$.sendToServer(playerTextPacket);
	$.scheduleEvent(3, "spam2");
}

function spam2(event) {
	var playerTextPacket = $.createPacket(ID_PLAYERTEXT);
	playerTextPacket.text = "spam bot"
	$.sendToServer(playerTextPacket);
	$.scheduleEvent(3, "spam");
}
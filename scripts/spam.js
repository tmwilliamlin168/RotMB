var ID_PLAYERTEXT = $.findPacketId("PLAYERTEXT");
var ID_CREATE_SUCCESS = $.findPacketId("CREATE_SUCCESS");
var ID_NEW_TICK = $.findPacketId("NEW_TICK");
var ID_MOVE = $.findPacketId("MOVE");

var pos;
var destX = 134;
var destY = 133;

function onServerPacket(event) {
	var packet = event.getPacket();
	switch (packet.id()) {
		case ID_CREATE_SUCCESS: {
			//start spam
			spam();
			break;
		}
		case ID_NEW_TICK: {
			if(!pos)
				pos = $.getPlayerData().pos;
			else if(pos.x == 0 && pos.y == 0)
				pos = $.getPlayerData().pos;
			var dx = destX-pos.x;
			var dy = destY-pos.y;
			var dratio = Math.min(1, (4+5.6*(10/75))/1000*packet.tickTime/Math.sqrt(dx*dx+dy*dy));
			dx *= dratio;
			dy *= dratio;
			pos.x += dx;
			pos.y += dy;
			var movePacket = $.createPacket(ID_MOVE);
			movePacket.tickId = packet.tickId;
			movePacket.time = $.getTime();
			movePacket.newPosition = pos;
			$.sendToServer(movePacket);
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
// core.js

var ID_HELLO = $.findPacketId("HELLO");
var ID_FAILURE = $.findPacketId("FAILURE");
var ID_RECONNECT = $.findPacketId("RECONNECT");
var ID_CREATE_SUCCESS = $.findPacketId("CREATE_SUCCESS");
var ID_MAPINFO = $.findPacketId("MAPINFO");
var ID_LOAD = $.findPacketId("LOAD");
var ID_UPDATE = $.findPacketId("UPDATE");
var ID_UPDATEACK = $.findPacketId("UPDATEACK");
var ID_PING = $.findPacketId("PING");
var ID_PONG = $.findPacketId("PONG");
var ID_NEW_TICK = $.findPacketId("NEW_TICK");
var ID_MOVE = $.findPacketId("MOVE");
var ID_AOE = $.findPacketId("AOE");
var ID_AOEACK = $.findPacketId("AOEACK");
var ID_GOTO = $.findPacketId("GOTO");
var ID_GOTOACK = $.findPacketId("GOTOACK");

var helloPacket = null;
var charId;

function onEnable(event, buildVersion, email, password, c) {
	charId = c;
	helloPacket = $.createPacket(ID_HELLO);
	helloPacket.buildVersion = buildVersion;
	helloPacket.gameId = -2;
	helloPacket.guid = email;
	helloPacket.password = password;
	helloPacket.keyTime = -1;
	$.connect(-2);
}

function onConnect(event) {
	$.echo("Connected to remote server.");
	$.echo("Sending HELLO packet.");
	$.sendToServer(helloPacket);
}

function onConnectFail(event) {
	$.echo("Connection to remote server failed!");
	$.kickUser();
}

function onDisconnect(event) {
	$.echo("Disconnected from remote server.");
	$.kickUser();
}

function onServerPacket(event) {
	var packet = event.getPacket();
	switch (packet.id()) {
		case ID_FAILURE: {
			$.echo(packet + " " + packet.errorId + " " + packet.errorDescription);
			$.kickUser();
			break;
		}
		case ID_RECONNECT: {
			var host;
			var port;
			if (packet.port == -1) { // -1 means same server/port you are on
				host = $.getRemoteHost();
				port = $.getRemotePort();
			} else {
				host = packet.host;
				port = packet.port;
			}
			$.setGameIdSocketAddress(packet.gameId, host, port);
			packet.host = "localhost";
			packet.port = 2050;
			break;
		}
		case ID_CREATE_SUCCESS: {
			$.echo("Server responded with CREATE_SUCCESS.");
			break;
		}
		case ID_MAPINFO: {
			$.echo("Received MAPINFO.");
			$.echo("Responding with LOAD.");
			var loadPacket = $.createPacket(ID_LOAD);
			loadPacket.charId = charId;
			loadPacket.isFromArena = false;
			$.sendToServer(loadPacket);
			break;
		}
		case ID_UPDATE: {
			//$.echo("Server sent UPDATE.");
			var updateAckPacket = $.createPacket(ID_UPDATEACK);
			$.sendToServer(updateAckPacket);
			break;
		}
		case ID_PING: {
			//$.echo("Server sent PING.");
			var pongPacket = $.createPacket(ID_PONG);
			pongPacket.time = $.getTime();
			pongPacket.serial = packet.serial;
			$.sendToServer(pongPacket);
			break;
		}
		case ID_NEW_TICK: {
			//$.echo("Server sent NEW_TICK.");
			var movePacket = $.createPacket(ID_MOVE);
			movePacket.tickId = packet.tickId;
			movePacket.time = $.getTime();
			movePacket.newPosition = $.getPlayerData().pos;
			$.sendToServer(movePacket);
			//$.getPlayerData().pos.x += 0.5;
			$.echo("tickId: "+packet.tickId);
			break;
		}
		case ID_AOE: {
			var aoeAckPacket = $.createPacket(ID_AOEACK);
			aoeAckPacket.time = $.getTime();
			aoeAckPacket.position = $.getPlayerData().pos;
			$.sendToServer(aoeAckPacket);
		}
		case ID_GOTO: {
			var gotoAckPacket = $.createPacket(ID_GOTOACK);
			gotoAckPacket.time = $.getTime();
			$.sendToServer(gotoAckPacket);
		}
	}
}
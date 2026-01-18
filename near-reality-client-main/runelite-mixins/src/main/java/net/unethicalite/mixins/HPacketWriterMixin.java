package net.unethicalite.mixins;

import net.runelite.api.mixins.*;
import net.runelite.api.packets.PacketBufferNode;
import net.runelite.rs.api.*;
import net.unethicalite.api.events.PacketSent;
import net.unethicalite.api.events.ServerPacketReceived;

@Mixin(RSPacketWriter.class)
public abstract class HPacketWriterMixin implements RSPacketWriter
{
	@Shadow("client")
	private static RSClient client;

	@Inject
	@FieldHook("serverPacket")
	public void onServerPacketChanged(int idx)
	{
		if (client != null && getServerPacket() != null)
		{
			ServerPacketReceived event = new ServerPacketReceived();
			RSPacketWriter packetWriter = client.getPacketWriter();
			RSPacketBuffer buffer = packetWriter.getPacketBuffer();
			RSServerPacket serverPacket = packetWriter.getServerPacket();

			event.setServerPacket(serverPacket);
			event.setPacketBuffer(buffer);
			event.setLength(packetWriter.getServerPacketLength());

			client.getCallbacks().post(event);
		}
	}

	@Inject
	@Override
	public void queuePacket(PacketBufferNode packetBufferNode)
	{
		packetBufferNode.getPacketBuffer().setAutomated(true);
		sendPacket((RSPacketBufferNode) packetBufferNode);
	}

	@Inject
	@Copy("addNode")
	@Replace("addNode")
	public void copy$addNode(RSPacketBufferNode packet)
	{
		if(packet.getClientPacket().getId() == 79)
			return;
		else {
			sendPacket(packet);
			client.getCallbacks().post(new PacketSent(packet));
		}
	}
}

package net.runelite.mixins;

import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.MethodHook;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import net.runelite.api.packets.PacketBufferNode;
import net.runelite.rs.api.RSClient;

import java.util.Arrays;
import java.util.List;

@Mixin(RSClient.class)
public abstract class CustomInterfaceMixin {

    @Inject
    private static final List<Integer> CUSTOM_INTERFACE_IDS = Arrays.asList(5003);

    @Shadow("client")
    private static RSClient client;



    @Inject
    @MethodHook(value = "menuAction")
    public static void checkCustomMenuOption(int componentChildIndex, int componentId, int var2, int opIndex, int componentItemId, String var5, String targetName, int var7, int var8) {
        if(!CUSTOM_INTERFACE_IDS.contains(componentId >> 16)) {}
        else {
            PacketBufferNode out;
            if (opIndex == 1) {
                out = client.preparePacket(client.createClientPacket(62, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 2) {
                out = client.preparePacket(client.createClientPacket(55, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 3) {
                out = client.preparePacket(client.createClientPacket(18, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 4) {
                out = client.preparePacket(client.createClientPacket(22, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 5) {
                out = client.preparePacket(client.createClientPacket(85, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 6) {
                out = client.preparePacket(client.createClientPacket(75, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 7) {
                out = client.preparePacket(client.createClientPacket(78, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 8) {
                out = client.preparePacket(client.createClientPacket(57, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 9) {
                out = client.preparePacket(client.createClientPacket(102, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }

            if (opIndex == 10) {
                out = client.preparePacket(client.createClientPacket(25, 8), client.getPacketWriter().getIsaacCipher());
                out.getPacketBuffer().writeInt(componentId);
                out.getPacketBuffer().writeShort(componentChildIndex);
                out.getPacketBuffer().writeShort(componentItemId);
                client.getPacketWriter().queuePacket(out);
            }
        }
    }

}

package com.near_reality.mixin;

import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSNPCComposition;

@Mixin(RSNPCComposition.class)
public abstract class NRNPCDefinitionMixin implements RSNPCComposition {

    @Copy("decode")
    @Replace("decode")
    void decode(RSBuffer var1) {
        while(true) {
            int var2 = var1.readUnsignedByte();
            if (var2 == 0) {
                return;
            }

            if(var2 >= 74 && var2 < 80) var1.readUnsignedShort();
            else if(var2 == 124) var1.readUnsignedShort();
            else decodeNext(var1, var2);
        }
    }
}

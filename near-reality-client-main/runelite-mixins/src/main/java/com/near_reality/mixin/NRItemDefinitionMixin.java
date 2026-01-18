package com.near_reality.mixin;

import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSItemComposition;

@Mixin(RSItemComposition.class)
public abstract class NRItemDefinitionMixin implements RSItemComposition {

    @Copy("decode")
    @Replace("decode")
    void decode(RSBuffer var1) {
        while(true) {
            int var2 = var1.readUnsignedByte();
            if (var2 == 0) {
                return;
            }

            if(var2 == 43) {
                var1.readUnsignedByte();
                while (true)
                {
                    int subopId = var1.readUnsignedByte() - 1;
                    if (subopId == -1)
                    {
                        break;
                    }
                    var1.readStringCp1252NullTerminated();
                }
            }
            else if(var2 == 3) var1.readStringCp1252NullTerminated();
            else decodeNext(var1, var2);
        }
    }
}

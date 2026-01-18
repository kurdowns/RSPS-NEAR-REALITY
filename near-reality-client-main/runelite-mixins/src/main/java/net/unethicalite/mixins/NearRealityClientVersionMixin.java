package net.unethicalite.mixins;

import net.runelite.api.mixins.*;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSPlatformInfo;

@Mixin(RSPlatformInfo.class)
public abstract class NearRealityClientVersionMixin implements RSPlatformInfo {

    @Inject
    public static final int SUB_VERSION = 3;

    @Shadow("client")
    private static RSClient client;

    @Inject
    @FieldHook("clockSpeed")
    public void rl$init(int cores)
    {
        setClockSpeed(SUB_VERSION);
    }

}

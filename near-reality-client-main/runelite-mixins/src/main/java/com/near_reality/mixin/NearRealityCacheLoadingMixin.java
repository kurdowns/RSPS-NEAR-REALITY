package com.near_reality.mixin;

import net.runelite.api.mixins.*;
import net.runelite.rs.api.RSArchive;
import net.runelite.rs.api.RSClient;

import java.util.Arrays;

@Mixin(RSClient.class)
public abstract class NearRealityCacheLoadingMixin {

    public static final int MAX_CLIENT_SCRIPTS = 35_000;
    public static final int MAX_WIDGETS = 15_000;
    public static final int MAX_INVENTORY_CONFIGS = 20_000;
    public static final int MAX_VARC_CONFIGS = 20_000;
    public static final int MAX_VARP_CONFIGS = 20_000;// hardcoded in client, but we hack it
    public static final int MAX_VARBIT_CONFIGS = 30_000;

    @Shadow("client")
    private static RSClient client;

    @SuppressWarnings({"unused", "InfiniteRecursion"})
    @Copy("newArchive")
    @Replace("newArchive")
    public static RSArchive copy$newArchive(int indexType, boolean var1, boolean var2, boolean var3, boolean var4)
    {
        final RSArchive archive = copy$newArchive(indexType, var1, var2, var3, var4);
        if (indexType == 12)
            archive.setOverwriteGroupCount(MAX_CLIENT_SCRIPTS);
        else if (indexType == 3)
            archive.setOverwriteGroupCount(MAX_WIDGETS);
        else if (indexType == 2) {
            archive.setOverwriteGroupFileCount(5, MAX_INVENTORY_CONFIGS);
            archive.setOverwriteGroupFileCount(14, MAX_VARBIT_CONFIGS);
            archive.setOverwriteGroupFileCount(15, MAX_VARC_CONFIGS);
            archive.setOverwriteGroupFileCount(16, MAX_VARP_CONFIGS);
            archive.setOverwriteGroupFileCount(19, MAX_VARC_CONFIGS);
        }
        return archive;
    }

    @Inject
    @MethodHook(value = "updateGameState", end = true)
    public static void onLoaded(int var0) {
        System.out.println("Loaded cache "+var0);
        if (var0 == 10) {
            client.getLogger().debug("Loaded cache");
            client.setVarps_main(Arrays.copyOf(client.getVarps(), MAX_VARP_CONFIGS));
            client.setVarps_temp(Arrays.copyOf(client.getServerVarps(), MAX_VARP_CONFIGS));
        }
    }
}

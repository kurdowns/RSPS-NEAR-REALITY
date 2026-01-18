package net.runelite.mixins;

import net.runelite.api.mixins.*;
import net.runelite.rs.api.*;

@Mixin(RSClient.class)
public abstract class RSPlayerTypeMixin implements RSClient {
    @Shadow("client")
    private static RSClient client;
}

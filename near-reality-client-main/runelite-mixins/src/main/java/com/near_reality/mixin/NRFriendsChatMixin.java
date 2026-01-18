package com.near_reality.mixin;

import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.rs.api.RSUserList;

import java.util.HashMap;


@Mixin(RSUserList.class)
public abstract class NRFriendsChatMixin implements RSUserList {

    @Replace("<init>")
    public NRFriendsChatMixin(int var1) {
        var1 = 2000;
        this.setCapacity(var1);
        this.setNameables(newContainer(var1));

        this.setUsernamesMap(new HashMap(var1 / 8));
        this.setPreviousUsernamesMap(new HashMap(var1 / 8));
    }
}

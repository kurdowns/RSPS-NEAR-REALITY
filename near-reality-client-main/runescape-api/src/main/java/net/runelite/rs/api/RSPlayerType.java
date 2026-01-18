package net.runelite.rs.api;

import net.runelite.mapping.Import;

public interface RSPlayerType {
    @Import("id")
    int getId();

    @Import("modIcon")
    int getModIcon();


}

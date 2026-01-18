package net.runelite.api.widgets;

public class WidgetUtil
{
    /**
     * Utility method that converts a component id to the interface it
     * belongs to.
     *
     * @param c component id
     * @return the interface id
     */
    public static int componentToInterface(int c)
    {
        return c >>> 16;
    }

    /**
     * Utility method that converts a component id to the id it is within
     * its interface.
     *
     * @param c component id
     */
    public static int componentToId(int c)
    {
        return c & 0xFFFF;
    }

    /**
     * Utility method that packs a component id from an interface id and child id.
     * @param interfaceId interface id
     * @param childId id within the interface
     * @return the component id
     */
    public static int packComponentId(int interfaceId, int childId)
    {
        return (interfaceId << 16) | childId;
    }
}
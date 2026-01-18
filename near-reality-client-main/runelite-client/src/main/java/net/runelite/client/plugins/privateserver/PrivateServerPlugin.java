package net.runelite.client.plugins.privateserver;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(name = "Private Server", hidden = true)
public final class PrivateServerPlugin extends Plugin {

    @Inject
    private Client client;

    @Override
    protected void startUp() {
        client.setModulus(RuneLite.MODULUS);
    }

    @Override
    protected void shutDown() throws Exception {
        client.setModulus(null);
    }

}

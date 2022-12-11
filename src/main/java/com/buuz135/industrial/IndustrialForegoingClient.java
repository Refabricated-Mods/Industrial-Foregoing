package com.buuz135.industrial;

import com.buuz135.industrial.proxy.client.ClientProxy;
import com.buuz135.industrial.proxy.network.BackpackOpenMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screens.Screen;

public class IndustrialForegoingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new ClientProxy().run();
    }
}

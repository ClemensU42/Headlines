package com.clemensu42.fabric;

import net.fabricmc.api.ModInitializer;

import com.clemensu42.HeadlinesCommon;

public final class HeadlinesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        HeadlinesCommon.init();
    }
}

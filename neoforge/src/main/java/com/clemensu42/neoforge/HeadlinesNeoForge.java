package com.clemensu42.neoforge;

import net.neoforged.fml.common.Mod;

import com.clemensu42.HeadlinesCommon;

@Mod(HeadlinesCommon.MOD_ID)
public final class HeadlinesNeoForge {
    public HeadlinesNeoForge() {
        // Run our common setup.
        HeadlinesCommon.init();
    }
}

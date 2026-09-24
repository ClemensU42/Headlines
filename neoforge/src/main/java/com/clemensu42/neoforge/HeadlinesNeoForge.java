package com.clemensu42.neoforge;

import com.google.common.base.Preconditions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import com.clemensu42.HeadlinesCommon;
import org.jetbrains.annotations.Nullable;

@Mod(HeadlinesCommon.MOD_ID)
public final class HeadlinesNeoForge {
    public HeadlinesNeoForge(ModContainer container) {
        // Run our common setup.
        HeadlinesCommon.init();

        @Nullable IEventBus modEventBus = container.getEventBus();
        Preconditions.checkNotNull(modEventBus);

        RegisterImpl.BLOCKS.register(modEventBus);
        RegisterImpl.BLOCK_ENTITY_TYPES.register(modEventBus);
        RegisterImpl.ITEMS.register(modEventBus);
        RegisterImpl.CREATIVE_MODE_TABS.register(modEventBus);
    }
}

package com.clemensu42;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class HeadlinesCommon {
    public static final String MOD_ID = "headlines";

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(MOD_ID, Registries.BLOCK);

    public static void init() {
        ITEMS.register();
        BLOCKS.register();
    }
}

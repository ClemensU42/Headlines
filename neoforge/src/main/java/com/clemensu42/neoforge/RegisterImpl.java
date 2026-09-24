package com.clemensu42.neoforge;

import com.clemensu42.HeadlinesCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegisterImpl {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, HeadlinesCommon.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HeadlinesCommon.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, HeadlinesCommon.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeadlinesCommon.MOD_ID);

    public static <T extends Block> Supplier<T> block(String id, Supplier<T> supplier){
        return BLOCKS.register(id, supplier);
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> blockEntityType(String id, Supplier<T> supplier){
        return BLOCK_ENTITY_TYPES.register(id, supplier);
    }

    public static <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks){
        return BlockEntityType.Builder.of(blockEntitySupplier::create, validBlocks).build(null);
    }

    public static <T extends Item> Supplier<T> item(String id, Supplier<T> supplier){
        return ITEMS.register(id, supplier);
    }

    public static <T extends CreativeModeTab> Supplier<T> creativeTab(String id, Supplier<T> supplier){
        return CREATIVE_MODE_TABS.register(id, supplier);
    }
}

package com.clemensu42.fabric;

import com.clemensu42.HeadlinesCommon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class RegisterImpl {
    public static <T extends Block> Supplier<T> block(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.BLOCK, HeadlinesCommon.resource(id), supplier.get());
        return () -> obj;

    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> blockEntityType(String id, Supplier<T> supplier){
        T obj = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, HeadlinesCommon.resource(id), supplier.get());
        return () -> obj;
    }

    public static <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks){
        return BlockEntityType.Builder.of(blockEntitySupplier::create, validBlocks).build();
    }

    public static <T extends Item> Supplier<T> item(String id, Supplier<T> supplier){
        T obj = Registry.register(BuiltInRegistries.ITEM, HeadlinesCommon.resource(id), supplier.get());
        return () -> obj;
    }

    public static <T extends CreativeModeTab> Supplier<T> creativeTab(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, HeadlinesCommon.resource(id), supplier.get());
        return () -> obj;
    }

}

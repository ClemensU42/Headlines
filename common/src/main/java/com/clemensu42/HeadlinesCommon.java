package com.clemensu42;

import com.clemensu42.world.block.LetterBoxBlock;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;

import java.util.function.Supplier;


public final class HeadlinesCommon {
    public static final String MOD_ID = "headlines";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        Blocks.init();
        Items.init();
        CreativeTabs.init();
    }

    public static ResourceLocation resource(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static class Blocks{

        public static final Supplier<LetterBoxBlock> LETTER_BOX_BLOCK = Register.block("letter_box",
                () -> new LetterBoxBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_LIGHT_GRAY)
                        .strength(2.5f)
                        .sound(SoundType.METAL)));

        public static void init(){
        }
    }

    public static class Items{

        public static final Supplier<BlockItem> LETTER_BOX = Register.item("letter_box",
                () -> new BlockItem(Blocks.LETTER_BOX_BLOCK.get(), new Item.Properties()));

        public static final Supplier<Item> NEWSPAPER = Register.item("newspaper",
                () -> new Item(new Item.Properties()));

        public static void init(){

        }
    }

    public static class CreativeTabs{

        public static final Supplier<CreativeModeTab> HEADLINES = Register.creativeTab("headlines", () ->
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                        .title(Component.translatable("itemGroup.headlines.headlines"))
                        .icon(() -> new ItemStack(Items.NEWSPAPER.get()))
                        .displayItems(((itemDisplayParameters, output) -> {
                            output.accept(Items.LETTER_BOX.get());
                        }))
                        .build());

        public static void init(){
        }
    }
}

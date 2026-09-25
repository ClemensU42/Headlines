package com.clemensu42.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrintingPressBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<PressBlockPart> PART = EnumProperty.create("part", PressBlockPart.class);


    public static final MapCodec<PrintingPressBlock> CODEC = simpleCodec(PrintingPressBlock::new);

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public PrintingPressBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (state.getValue(PART) == PressBlockPart.MAIN) return RenderShape.MODEL;
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPos abovePos = pos.above();
        BlockPos rightPos = pos.relative(context.getHorizontalDirection().getClockWise());

        if (pos.getY() < level.getMaxBuildHeight() - 1 &&
                level.getBlockState(abovePos).canBeReplaced(context) &&
                level.getBlockState(rightPos).canBeReplaced(context)){
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(PART, PressBlockPart.MAIN);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(PART, PressBlockPart.TOP), 3);
        level.setBlock(pos.relative(state.getValue(FACING).getCounterClockWise()), state.setValue(PART, PressBlockPart.RIGHT), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if(!level.isClientSide){
            PressBlockPart part = state.getValue(PART);
            BlockPos mainPartPos = part == PressBlockPart.MAIN ? pos : (
                    part == PressBlockPart.TOP ? pos.below() : pos.relative(state.getValue(FACING).getClockWise()));
            BlockEntity blockEntity = level.getBlockEntity(mainPartPos);
            /*if (blockEntity instanceof PrintingPressBlockEntity printingPressBlockEntity){
                printingPressBlockEntity.dropContents()
            }*/
            BlockPos topPos = mainPartPos.above();
            BlockPos rightPos = mainPartPos.relative(state.getValue(FACING).getCounterClockWise());
            if (state.getValue(PART) == PressBlockPart.MAIN){
                BlockState upState = level.getBlockState(topPos);
                BlockState rightState = level.getBlockState(rightPos);
                if (upState.is(this)) level.setBlock(topPos, Blocks.AIR.defaultBlockState(), 35);
                if (rightState.is(this)) level.setBlock(rightPos, Blocks.AIR.defaultBlockState(), 35);
            }
            else if (state.getValue(PART) == PressBlockPart.RIGHT){
                BlockState upState = level.getBlockState(topPos);
                BlockState mainState = level.getBlockState(mainPartPos);
                if (upState.is(this)) level.setBlock(topPos, Blocks.AIR.defaultBlockState(), 35);
                if (mainState.is(this)) level.setBlock(mainPartPos, Blocks.AIR.defaultBlockState(), 35);
            }
            else if (state.getValue(PART) == PressBlockPart.TOP){
                BlockState mainState = level.getBlockState(mainPartPos);
                BlockState rightState = level.getBlockState(rightPos);
                if (mainState.is(this)) level.setBlock(mainPartPos, Blocks.AIR.defaultBlockState(), 35);
                if (rightState.is(this)) level.setBlock(rightPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return null;
    }

    public enum PressBlockPart implements StringRepresentable {
        MAIN,
        TOP,
        RIGHT;

        @Override
        public @NotNull String getSerializedName() {
            if (this == MAIN) return "main";
            return this == TOP ? "top" : "right";
        }
    }
}

package com.clemensu42.world.block;

import com.clemensu42.ModUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MailboxBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);

    private static final VoxelShape TOP_SHAPE = makeTopShape();
    private static final VoxelShape BOTTOM_SHAPE = makeBottomShape();

    private static final Map<Direction, VoxelShape> ROTATED_TOP = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, ModUtil.rotateShape(Direction.NORTH, direction, TOP_SHAPE));
        }
    });

    private static final Map<Direction, VoxelShape> ROTATED_BOTTOM = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, ModUtil.rotateShape(Direction.NORTH, direction, BOTTOM_SHAPE));
        }
    });

    public static final MapCodec<MailboxBlock> CODEC = simpleCodec(MailboxBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public MailboxBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) return RenderShape.MODEL;
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return ROTATED_TOP.get(state.getValue(FACING));
        return ROTATED_BOTTOM.get(state.getValue(FACING));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) return Block.canSupportCenter(level, pos.below(), Direction.UP);
        return level.getBlockState(pos.below()).is(this);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide){
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos basePos = half == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockEntity blockEntity = level.getBlockEntity(basePos);
            /*if (blockEntity instanceof MailboxBlockEntity mailboxBlockEntity){
                mailboxBlockEntity.dropContents()
            }*/
            BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this)){
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return null;
    }

    private static VoxelShape makeTopShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Block.box(7, 0, 7, 9, 6, 9), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(4, 6, 1, 12, 14, 15), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Block.box(5, 0, 5, 11, 8, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(7, 8, 7, 9, 16, 9), BooleanOp.OR);
        return shape;
    }
}

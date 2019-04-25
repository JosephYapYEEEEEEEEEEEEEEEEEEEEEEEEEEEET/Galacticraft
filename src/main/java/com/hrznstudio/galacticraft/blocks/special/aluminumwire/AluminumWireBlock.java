package com.hrznstudio.galacticraft.blocks.special.aluminumwire;

import com.hrznstudio.galacticraft.api.blocks.WireBlock;
import com.hrznstudio.galacticraft.entity.GalacticraftBlockEntities;
import com.hrznstudio.galacticraft.util.WireConnectable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class AluminumWireBlock extends BlockWithEntity implements WireConnectable, WireBlock {

    private static BooleanProperty ATTACHED_NORTH = BooleanProperty.create("attached_north");
    private static BooleanProperty ATTACHED_EAST = BooleanProperty.create("attached_east");
    private static BooleanProperty ATTACHED_SOUTH = BooleanProperty.create("attached_south");
    private static BooleanProperty ATTACHED_WEST = BooleanProperty.create("attached_west");
    private static BooleanProperty ATTACHED_UP = BooleanProperty.create("attached_up");
    private static BooleanProperty ATTACHED_DOWN = BooleanProperty.create("attached_down");

    public AluminumWireBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateFactory().getDefaultState().with(ATTACHED_NORTH, false).with(ATTACHED_EAST, false).with(ATTACHED_SOUTH, false).with(ATTACHED_WEST, false).with(ATTACHED_UP, false).with(ATTACHED_DOWN, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = this.getDefaultState();
        state.with(ATTACHED_NORTH, false);
        state.with(ATTACHED_EAST, false);
        state.with(ATTACHED_SOUTH, false);
        state.with(ATTACHED_WEST, false);
        state.with(ATTACHED_UP, false);
        state.with(ATTACHED_DOWN, false);

        return state;
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1) {
        if (world_1.getBlockEntity(blockPos_1).getType() == GalacticraftBlockEntities.ALUMINUM_WIRE_TYPE) {
            ((AluminumWireBlockEntity) world_1.getBlockEntity(blockPos_1)).init();
        }
        WireNetwork.blockPlaced();

    }

    private BooleanProperty getPropForDirection(Direction dir) {
        switch (dir) {
            case SOUTH:
                return ATTACHED_SOUTH;
            case EAST:
                return ATTACHED_EAST;
            case WEST:
                return ATTACHED_WEST;
            case NORTH:
                return ATTACHED_NORTH;
            case UP:
                return ATTACHED_UP;
            case DOWN:
                return ATTACHED_DOWN;
        }
        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld world, BlockPos thisWire, BlockPos otherConnectable) {
        return blockState_1.with(getPropForDirection(direction_1), (
                !(blockState_2).isAir()
                        && blockState_2.getBlock() instanceof WireConnectable
                        // get opposite of direction so the WireConnectable can check from its perspective.
                        && (((WireConnectable) blockState_2.getBlock()).canWireConnect(world, direction_1.getOpposite(), thisWire, otherConnectable) != WireConnectionType.NONE)
        ));
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        super.appendProperties(stateFactory$Builder_1);
        stateFactory$Builder_1.with(ATTACHED_NORTH, ATTACHED_EAST, ATTACHED_SOUTH, ATTACHED_WEST, ATTACHED_UP, ATTACHED_DOWN);
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.MIPPED_CUTOUT;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getAmbientOcclusionLightLevel(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return 1.0F;
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Override
    public boolean canSuffocate(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return false;
    }

    @Override
    public boolean isSimpleFullBlock(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return false;
    }

    @Override
    public boolean allowsSpawning(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityType<?> entityType_1) {
        return false;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new AluminumWireBlockEntity();
    }

    @Override
    public WireConnectionType canWireConnect(IWorld world, Direction opposite, BlockPos connectionSourcePos, BlockPos connectionTargetPos) {
        return WireConnectionType.WIRE;
    }
}
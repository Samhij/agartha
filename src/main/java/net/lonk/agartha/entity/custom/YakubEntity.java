package net.lonk.agartha.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;

public class YakubEntity extends PathfinderMob {

    private BlockPos lastLightPos = null;
    private static final BlockState LIGHT_STATE = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15);
    private static final int LIGHT_UPDATE_FLAGS = 3;

    public YakubEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected void registerGoals() {
        // 1. Target the nearest player (highest priority)
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));

        // 2. Melee attack goal (high priority)
        // This makes it aggressively pursue the player
        this.targetSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));

        // 3. Fallback: If no target, wander around (low priority)
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        // 4. Look around
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.level().isClientSide()) return;
        boolean shouldEmitLight = this.isAlive();
        BlockPos currentPos = this.blockPosition();

        if (shouldEmitLight) {
            // Check if the entity has moved to a new block position
            if (lastLightPos == null || !currentPos.equals(lastLightPos)) {
                // Clean ip old light if it exists
                if (lastLightPos != null && this.level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                    this.level().setBlock(lastLightPos, Blocks.AIR.defaultBlockState(), LIGHT_UPDATE_FLAGS);
                }

                // Place new light at current position
                if (this.level().getBlockState(currentPos).canBeReplaced()) {
                    this.level().setBlock(currentPos, LIGHT_STATE, LIGHT_UPDATE_FLAGS);
                    lastLightPos = currentPos;
                } else {
                    // If the current block is not replaceable
                    lastLightPos = null;
                }
            }
        } else {
            // If the entity should not emit light
            removeLightBlock();
        }
    }

    // This is called when the entity is removed from the world
    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.level().isClientSide()) removeLightBlock();
    }

    // Custom helper method for cleanup
    private void removeLightBlock() {
        if (lastLightPos != null) {
            // Only attempt to remove if the block is actually our light block
            if (this.level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                this.level().setBlock(lastLightPos, Blocks.AIR.defaultBlockState(), LIGHT_UPDATE_FLAGS);
            }
            lastLightPos = null;
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean result = super.doHurtTarget(pEntity);
        if (result && pEntity instanceof Player && ((Player) pEntity).isDeadOrDying()) {
            this.remove(RemovalReason.DISCARDED);
        }
        return result;
    }
}

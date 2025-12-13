package net.lonk.agartha.entity.custom;

import net.lonk.agartha.entity.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YakubEntity extends PathfinderMob {

    private BlockPos lightPos = null;
    private static final BlockState LIGHT_BLOCK = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15);

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
    public void tick() {
        super.tick();

        if (this.isDeadOrDying()) return;
        if (!this.level().isClientSide()) {
            BlockPos current = this.blockPosition();

            if (lightPos == null || !lightPos.equals(current)) {
                if (lightPos != null) {
                    BlockState old = level().getBlockState(lightPos);
                    if (old.is(Blocks.LIGHT)) {
                        level().setBlock(lightPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                    lightPos = null;
                }

                BlockState here = level().getBlockState(current);
                if (here.isAir() || here.getCollisionShape(level(), current).isEmpty()) {
                    level().setBlock(current, LIGHT_BLOCK, 3);
                    lightPos = current;
                }
            }
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        if (!this.level().isClientSide() && lightPos != null) {
            BlockState old = level().getBlockState(lightPos);
            if (old.is(Blocks.LIGHT)) {
                level().setBlock(lightPos, Blocks.AIR.defaultBlockState(), 3);
            }
            lightPos = null;
        }
        super.die(pDamageSource);
    }

    @Override
    public void remove(RemovalReason pReason) {
        if (!this.level().isClientSide() && lightPos != null) {
            BlockState old = level().getBlockState(lightPos);
            if (old.is(Blocks.LIGHT)) {
                level().setBlock(lightPos, Blocks.AIR.defaultBlockState(), 3);
            }
            lightPos = null;
        }
        super.remove(pReason);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        float damage = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        DamageSource source = new DamageSource(ModDamageTypes.AGARTHAN_DAMAGE.getHolder().get(), this);

        if (pEntity instanceof Player player) {
            if (player.isDeadOrDying()) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

        return super.doHurtTarget(pEntity);
//        return pEntity.hurt(source, damage);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }
}

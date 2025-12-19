package net.lonk.agartha.entity.custom;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class YakubEntity extends PathfinderMob {
    private BlockPos lastLightPos = null;
    private static final BlockState LIGHT_STATE = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15);
    private static final int LIGHT_UPDATE_FLAGS = 3;

    protected static boolean canSpawn = true;

    // Climbing flag (same approach spiders use)
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(YakubEntity.class, EntityDataSerializers.BYTE);

    private int despawnTimer = 0;
    private final int despawnTime;

    public YakubEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        despawnTime = this.level().getGameRules().getInt(AgarthaMod.YAKUB_DESPAWN_TIME);

        if (!this.level().isClientSide()) {
            // Create the boots item stack
            ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
            // Prepare enchantments map
            Map<Enchantment, Integer> enchantments = new HashMap<>();

            // Add enchantments to the map
            enchantments.put(Enchantments.FROST_WALKER, 2);
            enchantments.put(Enchantments.UNBREAKING, 3);

            // Apply enchantments to the boots
            EnchantmentHelper.setEnchantments(enchantments, boots);

            // Equip the boots to the entity
            this.setItemSlot(EquipmentSlot.FEET, boots);
            // Set drop chance to 0 to prevent players from obtaining them
            this.setDropChance(EquipmentSlot.FEET, 0.0f);
        }
    }

    private final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 150.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    private boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    private void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & ~1);
        }
        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new AmphibiousPathNavigation(this, level());
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void registerGoals() {
        // 1. Target the nearest player (highest priority)
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));

        // 2. Melee attack goal (high priority)
        // This makes it aggressively pursue the player
        this.targetSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));

        // 3. Fallback: If no target, wander around (low priority)
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        // 4. Look around
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        despawnTimer += 1;
        if (!this.level().isClientSide() && despawnTimer >= despawnTime) {
            despawnTimer = 0;
            this.remove(RemovalReason.DISCARDED);
        }

        if (this.isInWater()) {
            this.remove(RemovalReason.DISCARDED);
        }
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
        canSpawn = true;
        if (!this.level().isClientSide()) removeLightBlock();
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        canSpawn = true;
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
        RegistryAccess registryAccess = this.level().registryAccess();
        Holder<DamageType> damageTypeHolder = registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ModDamageTypes.AGARTHAN_DAMAGE);

        float amount = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        DamageSource source = new DamageSource(damageTypeHolder, this);

        boolean result = pEntity.hurt(source, amount);
        if (result && pEntity instanceof Player && ((Player) pEntity).isDeadOrDying()) {
            this.remove(RemovalReason.DISCARDED);
        }
        return result;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        this.bossEvent.addPlayer(pServerPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        this.bossEvent.removePlayer(pServerPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setClimbing(this.horizontalCollision);
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
}

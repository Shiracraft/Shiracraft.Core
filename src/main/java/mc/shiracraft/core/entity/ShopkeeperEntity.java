package mc.shiracraft.core.entity;

import mc.shiracraft.core.gui.screen.ShopScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ShopkeeperEntity extends PathfinderMob {

    public static String ENTITY_ID = "shopkeeper";

    public ShopkeeperEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Attributes for registration in EntityAttributeCreationEvent.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                // Keep it sturdy so it doesn't die from minor bumps while testing.
                .add(Attributes.MAX_HEALTH, 20.0D)
                // Movement speed still matters for future pathfinding, but we'll prevent movement for now.
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void registerGoals() {
        // Intentionally empty for now.
        // Later: add goals (LookAtPlayerGoal, RandomLookAroundGoal, MoveToBlockGoal, etc.)
    }

    @Override
    public void tick() {
        super.tick();

        // Hard-lock position so it stays exactly where placed. This is a simple stop-gap.
        if (!this.level().isClientSide) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            this.hasImpulse = false;
        }
    }

    @Override
    public void aiStep() {
        // Prevent vanilla AI movement updates.
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        super.aiStep();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        // Future: defineEntityData accessors here, e.g.
        // this.entityData.define(SOME_DATA, defaultValue);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        // Nothing custom yet.
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        // Nothing custom yet.
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Open the shop UI on the client.
        if (this.level().isClientSide) {
            Minecraft.getInstance().setScreen(new ShopScreen(player));
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
        // No-op (since not pushable)
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Fully invulnerable: ignore all incoming damage.
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }
}

package net.nikgub.pyromancer.entities.attack_effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class AttackEffectEntity extends Entity {
    public int lifetime;
    private static final EntityDataAccessor<Boolean> FOLLOW_PLAYER = SynchedEntityData.defineId(AttackEffectEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> PLAYER_UUID = SynchedEntityData.defineId(AttackEffectEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(AttackEffectEntity.class, EntityDataSerializers.FLOAT);
    public AttackEffectEntity(EntityType<? extends AttackEffectEntity> entityType, Level level) {
        super(entityType, level);
        this.lifetime = 0;
    }
    public void setDoFollowPlayer(boolean b){
        this.entityData.set(FOLLOW_PLAYER, b);
    }
    public boolean getDoFollowPlayer(){
        return this.entityData.get(FOLLOW_PLAYER);
    }
    public void setPlayerUuid(UUID uuid){
        this.entityData.set(PLAYER_UUID, Optional.of(uuid));
    }
    public UUID getPlayerUuid(){
        return this.entityData.get(PLAYER_UUID).isPresent() ? this.entityData.get(PLAYER_UUID).get(): null;
    }
    public void setSize(float f){
        this.entityData.set(SIZE, f);
    }
    public float getSize(){
        return this.entityData.get(SIZE);
    }
    @Override
    protected void defineSynchedData(){
        this.entityData.define(FOLLOW_PLAYER, false);
        this.entityData.define(PLAYER_UUID, Optional.empty());
        this.entityData.define(SIZE, 1f);
    }
    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag){
        if(tag.contains("followPlayer", 1)) this.setDoFollowPlayer(tag.getBoolean("followPlayer"));
        if(tag.contains("owner", 1)) this.setPlayerUuid(tag.getUUID("owner"));
        if(tag.contains("float", 1)) this.setSize(1f);
    }
    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag){
        if (this.getPlayerUuid() != null) {
            tag.putUUID("owner", getPlayerUuid());
        }
        tag.putBoolean("followPlayer", this.getDoFollowPlayer());
        tag.putFloat("size", this.getSize());
    }
    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    @Override
    public void tick(){
        if(this.tickCount > lifetime) this.remove(RemovalReason.DISCARDED);
        if(this.getDoFollowPlayer() && this.getPlayerUuid() != null){
            Player player = this.level().getPlayerByUUID(this.getPlayerUuid());
            assert player != null;
            this.moveTo(player.getX(), player.getY(), player.getZ());
        }
    }
}

package xyz.nikgub.pyromancer.entities.attack_effects.pyranado;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xyz.nikgub.pyromancer.entities.attack_effects.AttackEffectEntity;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;
import xyz.nikgub.pyromancer.registries.vanila.DamageSourceRegistry;
import xyz.nikgub.pyromancer.util.EntityUtils;

import java.util.Arrays;
import java.util.UUID;

public class PyronadoEntity extends AttackEffectEntity {
    public float sizeCoefficient = 0f;
    public PyronadoEntity(EntityType<? extends AttackEffectEntity> entityType, Level level) {
        super(entityType, level);
        this.lifetime = 40;
    }
    @Override
    public void tick()
    {
        this.sizeCoefficient = (float) this.tickCount / 20f;
        if(!(this.level() instanceof ServerLevel serverLevel)) return;
        final float c = this.sizeCoefficient;
        final double R = 4 * c;
        final double X = this.getX();
        final double Y = this.getY();
        final double Z = this.getZ();
        final double sinK = R * Math.sin(Math.toRadians(this.tickCount * 18));
        final double cosK = R * Math.cos(Math.toRadians(this.tickCount * 18));
        serverLevel.sendParticles(ParticleTypes.FLAME, X + sinK, Y + this.tickCount * 0.1, Z + cosK, (int)(1 + 5 * c), 0.1, 0.1, 0.1, 0);
        serverLevel.sendParticles(ParticleTypes.FLAME, X - sinK, Y + this.tickCount * 0.1, Z - cosK, (int)(1 + 5 * c), 0.1, 0.1, 0.1, 0);
        if(this.tickCount > lifetime) this.remove(RemovalReason.DISCARDED);
        if(this.tickCount % 5 != 0) return;
        UUID ownerUUID = this.getPlayerUuid();
        if(ownerUUID == null) return;
        Player owner = this.level().getPlayerByUUID(ownerUUID);
        if(owner == null) return;
        double dx, dy, dz;
        double[] VALUES;
        double MAX;
        Vec3 direction;
        double cx = this.getX();
        double cy = this.getY() + 1;
        double cz = this.getZ();
        for(LivingEntity entity : EntityUtils.entityCollector(new Vec3(cx, cy, cz), 4 * this.sizeCoefficient, owner.level())){
            if(!entity.equals(owner)){
                entity.hurt(DamageSourceRegistry.pyronado(this, owner), (float) owner.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
                dx = this.getX() - entity.getX();
                dy = this.getY() - entity.getY();
                dz = this.getZ() - entity.getZ();
                VALUES = new double[]{
                        Mth.abs((float)dx),
                        Mth.abs((float)dy),
                        Mth.abs((float)dz)};
                MAX = Arrays.stream(VALUES).max().getAsDouble();
                dx = dx / MAX;
                dy = dy / MAX;
                dz = dz / MAX;
                direction = new Vec3(dx, dy, dz);
                entity.setDeltaMovement(direction);
            }
        }
    }
}

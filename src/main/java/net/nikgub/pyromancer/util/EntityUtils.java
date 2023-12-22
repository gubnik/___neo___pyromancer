package net.nikgub.pyromancer.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class EntityUtils {
    public static void shootProjectile(Projectile projectile, LivingEntity shooter, float speed, float inaccuracy){
        projectile.setOwner(shooter);
        projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.3, shooter.getZ());
        projectile.shoot(shooter.getLookAngle().x, shooter.getLookAngle().y, shooter.getLookAngle().z, speed, inaccuracy);
        shooter.level().addFreshEntity(projectile);
    }
    public static List<? extends LivingEntity> entityCollector(Vec3 center, double radius, Level level){
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(radius), e -> true).stream().sorted(Comparator.comparingDouble(
                entityFound -> entityFound.distanceToSqr(center))).toList();
    }
}

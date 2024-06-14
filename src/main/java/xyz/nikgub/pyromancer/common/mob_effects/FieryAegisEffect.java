package xyz.nikgub.pyromancer.common.mob_effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FieryAegisEffect extends SmartMobEffect{
    public FieryAegisEffect() {
        super(MobEffectCategory.BENEFICIAL, 240, 125, 0);
    }

    public float performAttack (float damageAmount, LivingEntity victim, LivingEntity attacker)
    {
        attacker.setSecondsOnFire((int) damageAmount + 1);
        attacker.knockback(damageAmount / 2f, victim.getX() - attacker.getX(), victim.getZ() - attacker.getZ());
        return damageAmount * 0.25f;
    }
}

package xyz.nikgub.pyromancer;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.common.entity.UnburnedSpiritEntity;
import xyz.nikgub.pyromancer.common.mob_effect.InfusionMobEffect;
import xyz.nikgub.pyromancer.common.mob_effect.OiledMobEffect;
import xyz.nikgub.pyromancer.common.util.ItemUtils;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;
import xyz.nikgub.pyromancer.registry.*;

@Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttackEffectEventHandling
{
    @SubscribeEvent
    public static void onLivingHurt (LivingHurtEvent event)
    {
        final LivingEntity target = event.getEntity();
        final DamageSource damageSource = event.getSource();
        final Entity cause = damageSource.getEntity();
        final Entity directCause = damageSource.getDirectEntity();
        final float damageAmount = event.getAmount();

        if (!target.isAlive())
        {
            event.setCanceled(event.isCancelable());
            return;
        }

        float multiplier = 1;
        float addition = 0;

        OiledMobEffect.tryIgnition(target, damageSource);
        if (GeneralUtils.isDirectDamage(damageSource))
        {
            if (!(directCause instanceof LivingEntity sourceEntity)) return;
            final ItemStack mainHand = sourceEntity.getMainHandItem();
            if (mainHand.getMaxStackSize() == 1)
            {
                multiplier += InfusionMobEffect.tryEffect(target, directCause, damageSource, damageAmount);
            }
            if (mainHand.getEnchantmentLevel(EnchantmentRegistry.CURSE_OF_CHAOS.get()) > 0)
            {
                multiplier += EnchantmentRegistry.Utils.tryCurseOfChaos(target);
            }
            if (target.canFreeze())
            {
                AttributeMap attributeMap = sourceEntity.getAttributes();
                if (attributeMap.hasAttribute(AttributeRegistry.COLD_BUILDUP.get()))
                {
                    double coldBuildup = attributeMap.getValue(AttributeRegistry.COLD_BUILDUP.get());
                    if (coldBuildup > 0)
                    {
                        multiplier += (target.isOnFire()) ? mainHand.getEnchantmentLevel(EnchantmentRegistry.MELT.get()) * 0.1F : 0.0F;
                        target.setTicksFrozen(target.getTicksFrozen() + (int) (coldBuildup * (damageAmount + 1)) + 1);
                        GeneralUtils.coverInParticles(target, ParticleTypes.SNOWFLAKE, 0.002);
                        if (target.isFullyFrozen())
                        {
                            final int frostburnModifier = target.hasEffect(MobEffectRegistry.FROSTBURN.get()) ? target.getEffect(MobEffectRegistry.FROSTBURN.get()).getAmplifier() : -1;
                            target.addEffect(new MobEffectInstance(MobEffectRegistry.FROSTBURN.get(), 100, frostburnModifier + 1, false, true, false));
                            target.setTicksFrozen(target.getTicksFrozen() + 200);
                        }
                    }
                }
            }
        }
        if (target.canFreeze())
        {
            final int frostburnModifier = target.hasEffect(MobEffectRegistry.FROSTBURN.get()) ? target.getEffect(MobEffectRegistry.FROSTBURN.get()).getAmplifier() : -1;
            if (frostburnModifier > 0)
            {
                addition += (float) (Math.sqrt(frostburnModifier + 1) * (1 + Math.log(frostburnModifier + 1)));
            }
        }
        if (target.hasEffect(MobEffectRegistry.FIERY_AEGIS.get()) && damageSource.getDirectEntity() instanceof LivingEntity attacker)
        {
            multiplier += MobEffectRegistry.FIERY_AEGIS.get().performAttack(damageAmount, target, attacker);
        }
        if (damageSource.getDirectEntity() instanceof Player attacker && ItemUtils.hasFullSetEquipped(attacker, ItemRegistry.HELLBLAZE_MONARCH_HELMET.get())
                && target.level() instanceof ServerLevel level && damageAmount >= 7 && (damageSource.is(DamageTypeDatagen.IS_PYROMANCY) || damageSource.is(DamageTypeDatagen.IS_EMBER)))
        {
            UnburnedSpiritEntity spirit = new UnburnedSpiritEntity(EntityTypeRegistry.UNBURNED_SPIRIT.get(), level);
            spirit.addToLevelForPlayerAt(level, attacker, target.position());
        }
        if (cause instanceof Player sourceEntity)
        {
            double armor_pierce = sourceEntity.getAttributeValue(AttributeRegistry.ARMOR_PIERCING.get());
            if (armor_pierce >= target.getAttributeValue(Attributes.ARMOR))
                addition += (float) armor_pierce;
        }

        event.setAmount(damageAmount * multiplier + addition);
    }
}

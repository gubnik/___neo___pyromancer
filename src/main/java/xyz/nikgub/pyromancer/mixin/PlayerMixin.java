package xyz.nikgub.pyromancer.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity
{
    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity pTarget, CallbackInfo callbackInfo) {
        if (!(this.getMainHandItem().getItem() == ItemRegistry.SPEAR_OF_MOROZ.get())) return;
        Player self = (Player) (Object) this;
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(self, pTarget)) return;
        if (pTarget.isAttackable()) {
            if (!pTarget.skipAttackInteraction(self)) {
                float f = (float)self.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float f1;
                if (pTarget instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getDamageBonus(self.getMainHandItem(), ((LivingEntity)pTarget).getMobType());
                } else {
                    f1 = EnchantmentHelper.getDamageBonus(self.getMainHandItem(), MobType.UNDEFINED);
                }

                float f2 = self.getAttackStrengthScale(0.5F);
                f *= 0.2F + f2 * f2 * 0.8F;
                f1 *= f2;
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    float i = (float)self.getAttributeValue(Attributes.ATTACK_KNOCKBACK); // Forge: Initialize self value to the attack knockback attribute of the player, which is by default 0
                    i += EnchantmentHelper.getKnockbackBonus(self);
                    if (self.isSprinting() && flag) {
                        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, self.getSoundSource(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && self.fallDistance > 0.0F && !self.onGround() && !self.onClimbable() && !self.isInWater() && !self.hasEffect(MobEffects.BLINDNESS) && !self.isPassenger() && pTarget instanceof LivingEntity;
                    flag2 = flag2 && !self.isSprinting();
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(self, pTarget, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2) {
                        f *= hitResult.getDamageModifier();
                    }

                    f += f1;
                    boolean flag3 = false;
                    double d0 = self.walkDist - self.walkDistO;
                    if (flag && !flag2 && !flag1 && self.onGround() && d0 < (double)self.getSpeed()) {
                        ItemStack itemstack = self.getItemInHand(InteractionHand.MAIN_HAND);
                        flag3 = itemstack.canPerformAction(net.minecraftforge.common.ToolActions.SWORD_SWEEP);
                    }

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspect(self);
                    if (pTarget instanceof LivingEntity) {
                        f4 = ((LivingEntity)pTarget).getHealth();
                        if (j > 0 && !pTarget.isOnFire()) {
                            flag4 = true;
                            pTarget.setSecondsOnFire(1);
                        }
                    }

                    Vec3 vec3 = pTarget.getDeltaMovement();
                    boolean flag5 = pTarget.hurt(self.damageSources().playerAttack(self), f);
                    if (flag5) {
                        if (i > 0) {
                            if (pTarget instanceof LivingEntity) {
                                ((LivingEntity)pTarget).knockback(i * 0.5F, Mth.sin(self.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(self.getYRot() * ((float)Math.PI / 180F)));
                            } else {
                                pTarget.push(-Mth.sin(self.getYRot() * ((float)Math.PI / 180F)) * i * 0.5F, 0.1D, Mth.cos(self.getYRot() * ((float)Math.PI / 180F)) * i * 0.5F);
                            }

                            self.setDeltaMovement(self.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                            //self.setSprinting(false);
                        }

                        if (flag3) {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(self) * f;

                            for(LivingEntity livingentity : self.level().getEntitiesOfClass(LivingEntity.class, self.getItemInHand(InteractionHand.MAIN_HAND).getSweepHitBox(self, pTarget))) {
                                double entityReachSq = Mth.square(self.getEntityReach()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update self to use canReach, since it uses closest-corner checks.
                                if (livingentity != self && livingentity != pTarget && !self.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStand) || !((ArmorStand)livingentity).isMarker()) && self.distanceToSqr(livingentity) < entityReachSq) {
                                    livingentity.knockback(0.4F, Mth.sin(self.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(self.getYRot() * ((float)Math.PI / 180F)));
                                    livingentity.hurt(self.damageSources().playerAttack(self), f3);
                                }
                            }

                            self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, self.getSoundSource(), 1.0F, 1.0F);
                            self.sweepAttack();
                        }

                        if (pTarget instanceof ServerPlayer && pTarget.hurtMarked) {
                            ((ServerPlayer)pTarget).connection.send(new ClientboundSetEntityMotionPacket(pTarget));
                            pTarget.hurtMarked = false;
                            pTarget.setDeltaMovement(vec3);
                        }

                        if (flag2) {
                            self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, self.getSoundSource(), 1.0F, 1.0F);
                            self.crit(pTarget);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, self.getSoundSource(), 1.0F, 1.0F);
                            } else {
                                self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, self.getSoundSource(), 1.0F, 1.0F);
                            }
                        }

                        if (f1 > 0.0F) {
                            self.magicCrit(pTarget);
                        }

                        self.setLastHurtMob(pTarget);
                        if (pTarget instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity)pTarget, self);
                        }

                        EnchantmentHelper.doPostDamageEffects(self, pTarget);
                        ItemStack itemstack1 = self.getMainHandItem();
                        Entity entity = pTarget;
                        if (pTarget instanceof net.minecraftforge.entity.PartEntity) {
                            entity = ((net.minecraftforge.entity.PartEntity<?>) pTarget).getParent();
                        }

                        if (!self.level().isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hurtEnemy((LivingEntity)entity, self);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(self, copy, InteractionHand.MAIN_HAND);
                                self.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (pTarget instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity)pTarget).getHealth();
                            self.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                pTarget.setSecondsOnFire(j * 4);
                            }

                            if (self.level() instanceof ServerLevel && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerLevel)self.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, pTarget.getX(), pTarget.getY(0.5D), pTarget.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        self.causeFoodExhaustion(0.1F);
                    } else {
                        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, self.getSoundSource(), 1.0F, 1.0F);
                        if (flag4) {
                            pTarget.clearFire();
                        }
                    }
                }
                self.resetAttackStrengthTicker(); // FORGE: Moved from beginning of attack() so that getAttackStrengthScale() returns an accurate value during all attack events

            }
        }
        callbackInfo.cancel();
    }
}

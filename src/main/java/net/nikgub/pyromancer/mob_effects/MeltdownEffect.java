package net.nikgub.pyromancer.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MeltdownEffect extends SmartMobEffect{
    public UUID MELTDOWN_MODIFIER_UUID = UUID.fromString("13caa470-1df0-11ee-be56-0242ac120002");
    public MeltdownEffect() {
        super(MobEffectCategory.HARMFUL, 156, 78, 12);
        this.addAttributeModifier(Attributes.ARMOR, MELTDOWN_MODIFIER_UUID.toString(), -1, AttributeModifier.Operation.ADDITION);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public @NotNull MobEffect addAttributeModifier(@NotNull Attribute attribute, @NotNull String uuidString, double amount, AttributeModifier.@NotNull Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uuidString), this::getDescriptionId, amount + 1, operation);
        this.getAttributeModifiers().put(attribute, attributemodifier);
        return this;
    }
}

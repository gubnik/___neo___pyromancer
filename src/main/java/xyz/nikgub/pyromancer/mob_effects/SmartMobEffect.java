package xyz.nikgub.pyromancer.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import xyz.nikgub.pyromancer.util.GeneralUtils;

public class SmartMobEffect extends MobEffect {
    public SmartMobEffect(MobEffectCategory category, int r, int g, int b) {
        super(category, GeneralUtils.rgbToColorInteger(r,g,b));
    }
}

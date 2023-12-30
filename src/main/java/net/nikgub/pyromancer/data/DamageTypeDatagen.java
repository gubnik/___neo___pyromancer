package net.nikgub.pyromancer.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.nikgub.pyromancer.PyromancerMod;

public class DamageTypeDatagen {
    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }
    public static ResourceKey<DamageType> HELLBLAZE_KEY = register("hellblaze");
    public static ResourceKey<DamageType> SIZZLING_HAND_KEY = register("sizzling_hand");
    public static ResourceKey<DamageType> FIREBRIAR_KEY = register("firebriar");
    public static ResourceKey<DamageType> BLAZING_JOURNAL_PROJECTION_KEY = register("blazing_journal_projection");
    public static DamageType HELLBLAZE = new DamageType(HELLBLAZE_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static DamageType SIZZLING_HAND = new DamageType(SIZZLING_HAND_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static DamageType FIREBRIAR = new DamageType(FIREBRIAR_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static DamageType BLAZING_JOURNAL_PROJECTION = new DamageType(BLAZING_JOURNAL_PROJECTION_KEY.location().getPath(), DamageScaling.NEVER, 0);
    public static void generate(BootstapContext<DamageType> bootstrap) {
        bootstrap.register(HELLBLAZE_KEY, HELLBLAZE);
        bootstrap.register(SIZZLING_HAND_KEY, SIZZLING_HAND);
        bootstrap.register(FIREBRIAR_KEY, FIREBRIAR);
        bootstrap.register(BLAZING_JOURNAL_PROJECTION_KEY, BLAZING_JOURNAL_PROJECTION);
    }
}

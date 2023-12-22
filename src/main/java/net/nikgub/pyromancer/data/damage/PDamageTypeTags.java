package net.nikgub.pyromancer.data.damage;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nikgub.pyromancer.PyromancerMod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PDamageTypeTags extends TagsProvider<DamageType> {
    public static TagKey<DamageType> create(String name)
    {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }
    public static TagKey<DamageType> IS_EMBER = create("is_ember");
    public PDamageTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, PyromancerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        tag(IS_EMBER)
                .add(PDamageType.HELLBLAZE_KEY);
        tag(DamageTypeTags.IS_PROJECTILE)
                .add(PDamageType.SIZZLING_HAND_KEY);
        tag(DamageTypeTags.IS_FIRE)
                .add(PDamageType.HELLBLAZE_KEY)
                .add(PDamageType.SIZZLING_HAND_KEY)
                .add(PDamageType.FIREBRIAR_KEY)
                .add(PDamageType.BLAZING_JOURNAL_PROJECTION_KEY);
    }
}

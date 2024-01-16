package net.nikgub.pyromancer.registries.custom;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.animations.AnimationList;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.registries.vanila.BlockRegistry;
import net.nikgub.pyromancer.util.BlockPosShaper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class EmberRegistry{
    public static String TAG_NAME = "PYROMANCER_EMBER";
    public static final ResourceKey<Registry<Ember>> EMBER_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(PyromancerMod.MOD_ID, "embers"));
    public static final DeferredRegister<Ember> EMBERS = DeferredRegister.create(EMBER_REGISTRY_KEY, PyromancerMod.MOD_ID);
    public static final Supplier<IForgeRegistry<Ember>> REGISTRY = EMBERS.makeRegistry(() -> new RegistryBuilder<Ember>().disableOverrides());
    public static RegistryObject<Ember> registerEmber(Ember ember)
    {
        return EMBERS.register(ember.getName(), () -> ember);
    }
    @Nullable
    public static Ember getEmberByName(String name)
    {
        return REGISTRY.get()
                .getValues()
                .stream()
                .filter((ember -> Objects.equals(ember.getName(), name)))
                .findFirst().orElse(null);
    }
    @Nullable
    public static Ember getFromItem(ItemStack itemStack)
    {
        return getEmberByName(itemStack.getOrCreateTag().getString(TAG_NAME));
    }
    public static RegistryObject<Ember> SOULFLAME_IGNITION = registerEmber(new Ember("soulflame_ignition", Ember.Type.SOULFLAME, Ember.GENERAL_WEAPONS, AnimationList.SOULFLAME_IGNITION,
            (player, weapon) ->
                    new BlockPosShaper(BlockPosShaper.Type.SPHERE, player.getOnPos().above(), 10, null).getValues().forEach(blockPos ->
                    {
                       if(player.level() instanceof ServerLevel serverLevel) serverLevel.setBlock(blockPos, BlockRegistry.PYROWOOD_PLANKS.get().defaultBlockState(), 3);
                    })));

}

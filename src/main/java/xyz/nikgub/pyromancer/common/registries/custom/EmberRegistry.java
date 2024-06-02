package xyz.nikgub.pyromancer.common.registries.custom;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.incandescent.common.util.BlockPosShaper;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.animations.EmberAnimationList;
import xyz.nikgub.pyromancer.common.registries.vanila.BlockRegistry;
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.ember.EmberType;

import java.util.Objects;
import java.util.function.Supplier;

public class EmberRegistry{
    public static String EMBER_TAG = "___PYROMANCER_EMBER_TAG___";
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
        return getEmberByName(itemStack.getOrCreateTag().getString(EMBER_TAG));
    }
    public static RegistryObject<Ember> SOULFLAME_IGNITION = registerEmber(new Ember("soulflame_ignition", EmberType.SOULFLAME, Ember.GENERAL_WEAPONS, EmberAnimationList.SOULFLAME_IGNITION,
            (player, weapon) ->
                    new BlockPosShaper(BlockPosShaper.Type.SPHERE, player.getOnPos().above(), 10, null).getValues().forEach(blockPos ->
                    {
                       if(player.level() instanceof ServerLevel serverLevel) serverLevel.setBlock(blockPos, BlockRegistry.PYROWOOD_PLANKS.get().defaultBlockState(), 3);
                    })));

}

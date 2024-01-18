package xyz.nikgub.pyromancer.registries.vanila;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

public class AttributeRegistry {
    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PyromancerMod.MOD_ID);
    public static RegistryObject<Attribute> BLAZE_CONSUMPTION = registerAttribute(new RangedAttribute("pyromancer.blaze_consumption", 0, 0, 512));
    public static RegistryObject<Attribute> PYROMANCY_DAMAGE = registerAttribute(new RangedAttribute("pyromancer.pyromancy_damage", 4, 0, 512));
    private static RegistryObject<Attribute> registerAttribute(Attribute attribute)
    {
        return ATTRIBUTES.register(attribute.getDescriptionId(), () -> attribute);
    }
}

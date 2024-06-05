package xyz.nikgub.pyromancer.common.registries;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

public class AttributeRegistry {
    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PyromancerMod.MOD_ID);

    public static RegistryObject<Attribute> ARMOR_PIERCING = registerAttribute(new RangedAttribute("pyromancer.armor_piercing", 0, -30, 30));

    public static RegistryObject<Attribute> BLAZE_CONSUMPTION = registerAttribute(new RangedAttribute("pyromancer.blaze_consumption", 0, 0, Integer.MAX_VALUE));

    public static RegistryObject<Attribute> PYROMANCY_DAMAGE = registerAttribute(new RangedAttribute("pyromancer.pyromancy_damage", 0, 0, Integer.MAX_VALUE));

    private static RegistryObject<Attribute> registerAttribute(Attribute attribute)
    {
        return ATTRIBUTES.register(attribute.getDescriptionId(), () -> attribute);
    }
}

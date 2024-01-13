package net.nikgub.pyromancer.registries.vanila;

import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.ArrayList;
import java.util.List;

public class WoodTypesRegistry {
    public static List<WoodType> VALUES = new ArrayList<>();
    public static final WoodType PYROWOOD = makeNewWoodType(new WoodType("pyrowood", BlockSetTypeRegistry.PYROWOOD));
    private static WoodType makeNewWoodType(WoodType woodType)
    {
        WoodType.register(woodType);
        VALUES.add(woodType);
        return woodType;
    }
}

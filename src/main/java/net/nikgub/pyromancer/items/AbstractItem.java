package net.nikgub.pyromancer.items;

import net.minecraft.world.item.Item;

import java.util.UUID;

public abstract class AbstractItem extends Item {
    public AbstractItem(Properties p_41383_) {
        super(p_41383_);
    }
    public static UUID BASE_DAMAGE = Item.BASE_ATTACK_DAMAGE_UUID;
    public static UUID BASE_SPEED = BASE_ATTACK_SPEED_UUID;
}

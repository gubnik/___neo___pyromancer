/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.contract.AccursedContractEntry;
import xyz.nikgub.pyromancer.common.entity.FrostcopperGolemEntity;
import xyz.nikgub.pyromancer.common.entity.RimegazerEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ContractRegistry
{
    private static final ResourceKey<Registry<AccursedContractEntry<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(PyromancerMod.MOD_ID, "contracts"));
    public static final DeferredRegister<AccursedContractEntry<?>> CONTRACTS = DeferredRegister.create(REGISTRY_KEY, PyromancerMod.MOD_ID);
    public static final Supplier<IForgeRegistry<AccursedContractEntry<?>>> REGISTRY = CONTRACTS.makeRegistry(() -> new RegistryBuilder<AccursedContractEntry<?>>().disableOverrides());

    public static boolean isContractMob(Entity entity)
    {
        return CONTRACTS.getEntries().stream().map(RegistryObject::get).anyMatch(accursedContractEntry -> entity.getType().equals(accursedContractEntry.getEntityToSummon()));
    }

    public static RegistryObject<AccursedContractEntry<Zombie>> ZOMBIE = CONTRACTS.register("zombie",
        () -> new AccursedContractEntry<>(EntityType.ZOMBIE, 1, (Level pLevel) ->
        {
            Zombie zombie = EntityType.ZOMBIE.create(pLevel);
            if (zombie == null) return null;
            final ItemStack helmet = Items.IRON_HELMET.getDefaultInstance();
            helmet.enchant(Enchantments.UNBREAKING, 255);
            zombie.setItemSlot(EquipmentSlot.HEAD, helmet);
            Map<EquipmentSlot, List<Item>> set = Map.of(
                EquipmentSlot.CHEST, List.of(Items.IRON_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.LEATHER_CHESTPLATE),
                EquipmentSlot.LEGS, List.of(Items.IRON_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.LEATHER_LEGGINGS)
            );
            for (var entry : set.entrySet())
            {
                if (zombie.getRandom().nextInt(0, 6) != 0) continue;
                zombie.setItemSlot(entry.getKey(), entry.getValue().get(zombie.getRandom().nextInt(0, entry.getValue().size())).getDefaultInstance());
            }
            return zombie;
        }));

    public static RegistryObject<AccursedContractEntry<Stray>> STRAY = CONTRACTS.register("stray",
        () -> new AccursedContractEntry<>(EntityType.STRAY, 3, (Level pLevel) ->
        {
            Stray stray = EntityType.STRAY.create(pLevel);
            if (stray == null) return null;
            final ItemStack helmet = Items.IRON_HELMET.getDefaultInstance();
            helmet.enchant(Enchantments.UNBREAKING, 255);
            stray.setItemSlot(EquipmentSlot.HEAD, helmet);
            Map<EquipmentSlot, List<Item>> set = Map.of(
                EquipmentSlot.CHEST, List.of(Items.IRON_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.LEATHER_CHESTPLATE),
                EquipmentSlot.FEET, List.of(Items.IRON_BOOTS, Items.CHAINMAIL_BOOTS, Items.LEATHER_BOOTS)
            );
            for (var entry : set.entrySet())
            {
                if (stray.getRandom().nextInt(0, 4) != 0) continue;
                stray.setItemSlot(entry.getKey(), entry.getValue().get(stray.getRandom().nextInt(0, entry.getValue().size())).getDefaultInstance());
            }
            if (stray.getRandom().nextInt(0, 1) == 0)
            {
                stray.setItemSlot(EquipmentSlot.MAINHAND, Items.BOW.getDefaultInstance());
            } else
            {
                stray.setItemSlot(EquipmentSlot.MAINHAND, Items.IRON_SWORD.getDefaultInstance());
            }
            return stray;
        }));

    public static RegistryObject<AccursedContractEntry<FrostcopperGolemEntity>> FROSTCOPPER_GOLEM = CONTRACTS.register("frostcopper_golem",
        () -> new AccursedContractEntry<>(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), 8));

    public static RegistryObject<AccursedContractEntry<RimegazerEntity>> RIMEGAZER = CONTRACTS.register("rimegazer",
        () -> new AccursedContractEntry<>(EntityTypeRegistry.RIMEGAZER.get(), 30));
}

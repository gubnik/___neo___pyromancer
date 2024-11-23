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
package xyz.nikgub.pyromancer.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item.IExtensibleTooltipItem;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.item_capability.BlazingJournalCapability;

import javax.annotation.Nullable;
import java.util.List;

/**
 * General class for quills. <p>
 * Quills are special items that can be inserted into {@link BlazingJournalItem}, consult {@link BlazingJournalCapability} <p>
 * Effects of quills are called when Blazing Journal activates an additional attack as per its defined enchantments
 */
public abstract class QuillItem extends Item implements IExtensibleTooltipItem
{
    public static final String QUILL_RENDER_TAG = "PYROMANCER_QUILL_SMART_RENDER_TAG";

    public QuillItem (Properties properties)
    {
        super(properties.stacksTo(1));
    }

    /**
     * Function that launches the attack
     * Do not invoke {@link Entity#hurt(DamageSource, float)} inside this method with damage type that could
     * activate the attack once again, as it will drown the stack and cause the game to crash
     * * Called in {@link xyz.nikgub.pyromancer.PyromancerMod.ForgeEvents#livingAttackEvent(LivingAttackEvent)}
     *
     * @param player       Player responsible for attack
     * @param target       Target of the attack
     * @param weaponStack  Weapon that the damage was dealt with
     * @param journalStack Journal in which the quill is located
     */
    public abstract void getAttack (Player player, Entity target, ItemStack weaponStack, ItemStack journalStack);

    /**
     * Defines whether the quill effect is to be launched
     * Called in {@link xyz.nikgub.pyromancer.PyromancerMod.ForgeEvents#livingAttackEvent(LivingAttackEvent)}
     *
     * @param damageSource Damage source of the attack
     * @param player       Player responsible for attack
     * @param target       Target of the attack
     * @param weaponStack  Weapon that the damage was dealt with
     * @param journalStack Journal in which the quill is located
     * @return Will the {@link #getAttack(Player, Entity, ItemStack, ItemStack)} be activated
     */
    public abstract boolean isActivated (DamageSource damageSource, Player player, Entity target, ItemStack weaponStack, ItemStack journalStack);

    public abstract float getDefaultPyromancyDamageBonus ();

    public abstract int getDefaultBlazeCostBonus ();

    @Override
    public final void appendHoverText (@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        this.gatherTooltipLines(list, "pyromancer.hidden_desc", "desc", PyromancerConfig.descTooltipKey);
        this.gatherTooltipLines(list, "pyromancer.hidden_lore", "lore", PyromancerConfig.loreTooltipKey);
    }
}

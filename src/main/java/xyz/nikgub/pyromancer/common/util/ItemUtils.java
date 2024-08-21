package xyz.nikgub.pyromancer.common.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;

public class ItemUtils
{
    public static ItemStack guessJournal(Player player)
    {
        if(player.getOffhandItem().getItem() instanceof BlazingJournalItem) return player.getOffhandItem();
        else if(player.getMainHandItem().getItem() instanceof BlazingJournalItem) return player.getMainHandItem();
        else return ItemStack.EMPTY;
    }

    public static int getBlaze(Player player)
    {
        ItemStack supposedJournal = guessJournal(player);
        if(supposedJournal == ItemStack.EMPTY) return 0;
        return supposedJournal.getOrCreateTag().getInt(BlazingJournalItem.BLAZE_TAG_NAME);
    }

    public static void setBlaze(Player player, int val)
    {
        ItemStack supposedJournal = guessJournal(player);
        if(supposedJournal == ItemStack.EMPTY) return;
        supposedJournal.getOrCreateTag().putInt(BlazingJournalItem.BLAZE_TAG_NAME, Mth.clamp(val, 0, 512));
    }

    public static void changeBlaze(Player player, int val)
    {
        setBlaze(player, getBlaze(player) + val);
    }

    public static boolean hasFullSetEquipped(LivingEntity entity, ArmorItem checkedArmorItem)
    {
        boolean b = true;
        for(EquipmentSlot equipmentSlot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET})
        {
            if (!(entity.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem armorItem) || !armorItem.getMaterial().equals(checkedArmorItem.getMaterial()))
            {
                b = false;
                break;
            }
        }
        return b;
    }
}

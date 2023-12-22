package net.nikgub.pyromancer.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.nikgub.pyromancer.items.blazing_journal.BlazingJournalItem;

public class ItemUtils {
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
        supposedJournal.getOrCreateTag().putInt(BlazingJournalItem.BLAZE_TAG_NAME, val);
    }
    public static void changeBlaze(Player player, int val)
    {
        ItemStack supposedJournal = guessJournal(player);
        if(supposedJournal == ItemStack.EMPTY) return;
        supposedJournal.getOrCreateTag().putInt(BlazingJournalItem.BLAZE_TAG_NAME, getBlaze(player) + val);
    }
}

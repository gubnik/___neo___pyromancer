package xyz.nikgub.pyromancer.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.registries.vanila.TierRegistry;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements net.minecraftforge.common.extensions.IForgeItemStack {
    @Shadow
    public abstract Item getItem();
    @Shadow
    private CompoundTag tag;

    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    public void enchantMixinHead(Enchantment enchantment, int level, CallbackInfo callbackInfo) {
        final ItemStack self = (ItemStack) (Object) this;
        if (!(self.getItem() instanceof TieredItem tieredItem && tieredItem.getTier().equals(TierRegistry.AMBER))) return;
        self.getOrCreateTag();
        if (!this.tag.contains("Enchantments", 9)) {
            this.tag.put("Enchantments", new ListTag());
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        ListTag listtag = this.tag.getList("Enchantments", 10);
        if (random.nextInt(0, 11) <= 2) listtag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), (byte)(level + 1)));
        else listtag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), (byte)level));
        callbackInfo.cancel();
    }
}

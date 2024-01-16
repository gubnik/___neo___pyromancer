package net.nikgub.pyromancer.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.items.AbstractItem;
import net.nikgub.pyromancer.items.EmberItem;
import net.nikgub.pyromancer.items.INotStupidTooltip;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;
import static net.minecraft.world.item.ItemStack.appendEnchantmentNames;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements net.minecraftforge.common.extensions.IForgeItemStack {
    private static int tick = 0;
    private static boolean reverseTicking = false;
    private static final int TICK_LIMIT = 200;
    @Shadow
    private static final Component DISABLED_ITEM_TOOLTIP = Component.translatable("item.disabled").withStyle(ChatFormatting.RED);
    @Shadow
    private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true);
    @Shadow
    public abstract CompoundTag getTagElement(String string);
    @Shadow
    public abstract Item getItem();
    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    public void getHoverNameMixinHead(CallbackInfoReturnable<Component> retVal) {
        ItemStack self = (ItemStack) (Object) this;
        if(Ember.emberItemStackPredicate(self) || self.getItem() instanceof EmberItem) {
            assureCorrectTick();
            Ember ember = EmberRegistry.getFromItem(self);
            if(ember == null) return;
            CompoundTag compoundtag = this.getTagElement("display");
            if (compoundtag != null && compoundtag.contains("Name", 8)) {
                try {
                    MutableComponent component = Component.Serializer.fromJson(compoundtag.getString("Name"));
                    if (component != null) {
                        component = component.withStyle(component.getStyle().withColor(ember.getType().getTextColorFunction().apply(tick)));
                        retVal.setReturnValue(component);
                    }
                    compoundtag.remove("Name");
                } catch (Exception exception) {
                    compoundtag.remove("Name");
                }
            }
            MutableComponent defaultComponent = this.getItem().getName(self).copy();
            defaultComponent = defaultComponent.withStyle(defaultComponent.getStyle().withColor(ember.getType().getTextColorFunction().apply(tick)));
            retVal.setReturnValue(defaultComponent);
        }
    }
    private static void assureCorrectTick()
    {
        if((reverseTicking && tick == 0)
        || (!reverseTicking && tick >= TICK_LIMIT))
        {
            reverseTicking = !reverseTicking;
        }
        if (reverseTicking)
        {
            tick--;
        } else tick++;
    }
    @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
    public void getTooltipLines(@Nullable Player player, TooltipFlag flag1, CallbackInfoReturnable<List<Component>> retVal) {
        ItemStack self = (ItemStack) (Object) this;
        if(!(self.getItem() instanceof INotStupidTooltip notStupidTooltip)) return;
        List<Component> list = Lists.newArrayList();
        MutableComponent mutablecomponent = Component.empty().append(self.getHoverName()).withStyle(self.getRarity().getStyleModifier());
        if (self.hasCustomHoverName()) {
            mutablecomponent.withStyle(ChatFormatting.ITALIC);
        }

        list.add(mutablecomponent);
        if (!flag1.isAdvanced() && !self.hasCustomHoverName() && self.is(Items.FILLED_MAP)) {
            Integer integer = MapItem.getMapId(self);
            if (integer != null) {
                list.add(Component.literal("#" + integer).withStyle(ChatFormatting.GRAY));
            }
        }

        int j = this.getHideFlags();
        if (shouldShowInTooltip(j, ItemStack.TooltipPart.ADDITIONAL)) {
            this.getItem().appendHoverText(self, player == null ? null : player.level(), list, flag1);
        }

        if (self.hasTag()) {
            if (shouldShowInTooltip(j, ItemStack.TooltipPart.UPGRADES) && player != null) {
                ArmorTrim.appendUpgradeHoverText(self, player.level().registryAccess(), list);
            }

            if (shouldShowInTooltip(j, ItemStack.TooltipPart.ENCHANTMENTS)) {
                appendEnchantmentNames(list, self.getEnchantmentTags());
            }

            if (self.getOrCreateTag().contains("display", 10)) {
                CompoundTag compoundtag = self.getOrCreateTag().getCompound("display");
                if (shouldShowInTooltip(j, ItemStack.TooltipPart.DYE) && compoundtag.contains("color", 99)) {
                    if (flag1.isAdvanced()) {
                        list.add(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", compoundtag.getInt("color"))).withStyle(ChatFormatting.GRAY));
                    } else {
                        list.add(Component.translatable("item.dyed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    }
                }

                if (compoundtag.getTagType("Lore") == 9) {
                    ListTag listtag = compoundtag.getList("Lore", 8);

                    for (int i = 0; i < listtag.size(); ++i) {
                        String s = listtag.getString(i);

                        try {
                            MutableComponent mutablecomponent1 = Component.Serializer.fromJson(s);
                            if (mutablecomponent1 != null) {
                                list.add(ComponentUtils.mergeStyles(mutablecomponent1, LORE_STYLE));
                            }
                        } catch (Exception exception) {
                            compoundtag.remove("Lore");
                        }
                    }
                }
            }
        }
        // the important part
        if (shouldShowInTooltip(j, ItemStack.TooltipPart.MODIFIERS)) {
            for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> multimap = self.getAttributeModifiers(equipmentslot);
                if (!multimap.isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    list.add(Component.translatable("item.modifiers." + equipmentslot.getName()).withStyle(ChatFormatting.GRAY));

                    for(Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        double d0 = attributemodifier.getAmount();
                        boolean flag = false;
                        ChatFormatting chatFormatting = ChatFormatting.RED;
                        if (player != null) {
                            Map<Attribute, Pair<UUID, ChatFormatting>> special = notStupidTooltip.specialColoredUUID();
                            for(Attribute attribute : special.keySet().stream().toList()) {
                                if(attributemodifier.getId() == special.get(attribute).getFirst())
                                {
                                    d0 += player.getAttributeValue(attribute);
                                    d0 += notStupidTooltip.getAdditionalPlayerBonus().apply(player);
                                    chatFormatting = special.get(attribute).getSecond();
                                    flag = true;
                                }
                            }

                            // vvv Default behaviour vvv
                            if (attributemodifier.getId() == AbstractItem.BASE_DAMAGE) {
                                d0 += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                                d0 += EnchantmentHelper.getDamageBonus(self, MobType.UNDEFINED);
                                flag = true;
                            } else if (attributemodifier.getId() == AbstractItem.BASE_SPEED) {
                                d0 += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                                flag = true;
                            }
                        }

                        double d1;
                        if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                            if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                                d1 = d0 * 10.0D;
                            } else {
                                d1 = d0;
                            }
                        } else {
                            d1 = d0 * 100.0D;
                        }

                        if (flag) {
                            list.add(CommonComponents.space()
                                    .append(Component.translatable("attribute.modifier.equals." + attributemodifier.getOperation().toValue(),
                                            ATTRIBUTE_MODIFIER_FORMAT.format(d1),
                                            Component.translatable(entry.getKey().getDescriptionId()))).withStyle(chatFormatting));
                        } else if (d0 > 0.0D) {
                            list.add(Component.translatable("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                        } else if (d0 < 0.0D) {
                            d1 *= -1.0D;
                            list.add(Component.translatable("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.RED));
                        }
                    }
                }
            }
        }
        // the important part ends there
        if (self.hasTag()) {
            if (shouldShowInTooltip(j, ItemStack.TooltipPart.UNBREAKABLE) && self.getOrCreateTag().getBoolean("Unbreakable")) {
                list.add(Component.translatable("item.unbreakable").withStyle(ChatFormatting.BLUE));
            }

            if (shouldShowInTooltip(j, ItemStack.TooltipPart.CAN_DESTROY) && self.getOrCreateTag().contains("CanDestroy", 9)) {
                ListTag listtag1 = self.getOrCreateTag().getList("CanDestroy", 8);
                if (!listtag1.isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    list.add(Component.translatable("item.canBreak").withStyle(ChatFormatting.GRAY));

                    for (int k = 0; k < listtag1.size(); ++k) {
                        list.addAll(expandBlockState(listtag1.getString(k)));
                    }
                }
            }

            if (shouldShowInTooltip(j, ItemStack.TooltipPart.CAN_PLACE) && self.getOrCreateTag().contains("CanPlaceOn", 9)) {
                ListTag listtag2 = self.getOrCreateTag().getList("CanPlaceOn", 8);
                if (!listtag2.isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    list.add(Component.translatable("item.canPlace").withStyle(ChatFormatting.GRAY));

                    for (int l = 0; l < listtag2.size(); ++l) {
                        list.addAll(expandBlockState(listtag2.getString(l)));
                    }
                }
            }
        }
        if (flag1.isAdvanced()) {
            if (self.isDamaged()) {
                list.add(Component.translatable("item.durability", self.getMaxDamage() - self.getDamageValue(), self.getMaxDamage()));
            }

            list.add(Component.literal(ForgeRegistries.ITEMS.getKey(self.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
            if (self.hasTag()) {
                list.add(Component.translatable("item.nbt_tags", self.getOrCreateTag().getAllKeys().size()).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        if (player != null && !this.getItem().isEnabled(player.level().enabledFeatures())) {
            list.add(DISABLED_ITEM_TOOLTIP);
        }

        net.minecraftforge.event.ForgeEventFactory.onItemTooltip(self, player, list, flag1);
        retVal.setReturnValue(list);
        retVal.cancel();
    }
    // what follows is a terrible workaround to provide vanilla functions from ItemStack without using accesstransformer.cfg
    private static boolean shouldShowInTooltip(int p_41627_, ItemStack.TooltipPart p_41628_) {
        return (p_41627_ & p_41628_.getMask()) == 0;
    }
    public int getHideFlags() {
        ItemStack self = (ItemStack) (Object) this;
        return self.hasTag() && self.getOrCreateTag().contains("HideFlags", 99) ? self.getOrCreateTag().getInt("HideFlags") : this.getItem().getDefaultTooltipHideFlags(self);
    }
    private static Collection<Component> expandBlockState(String p_41762_) {
        try {
            return BlockStateParser.parseForTesting(BuiltInRegistries.BLOCK.asLookup(), p_41762_, true).map(
                    (p_220162_) -> Lists.newArrayList(p_220162_.blockState()
                            .getBlock()
                            .getName()
                            .withStyle(ChatFormatting.DARK_GRAY)), (p_220164_) -> p_220164_.tag().stream().map((p_220172_) -> p_220172_.value().getName().withStyle(ChatFormatting.DARK_GRAY)).collect(Collectors.toList()));
        } catch (CommandSyntaxException commandsyntaxexception) {
            return Lists.newArrayList(Component.literal("missingno").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}

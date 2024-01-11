package net.nikgub.pyromancer.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GeneralUtils {
    public static int rgbToColorInteger(int red, int green, int blue){
        return 65536 * red + 256 * green + blue;
    }
    public static int rgbaToColorInteger(int red, int green, int blue, int alpha){
        return 16777216 * alpha + 65536 * red + 256 * green + blue;
    }
    public static boolean hasCompletedTheAdvancement(ServerPlayer serverPlayer, Advancement advancement){
        if(advancement == null) return false;
        return serverPlayer.getAdvancements()
                .getOrStartProgress(advancement)
                .isDone();
    }
    public static void addAdvancement(ServerPlayer serverPlayer, ResourceLocation resourceLocation)
    {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(resourceLocation);
        if(advancement == null) return;
        if(!hasCompletedTheAdvancement(serverPlayer, advancement))
        {
            AdvancementProgress advancementProgress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
            {
                for (String s : advancementProgress.getRemainingCriteria()) serverPlayer.getAdvancements().award(advancement, s);
            }
        }
    }
}

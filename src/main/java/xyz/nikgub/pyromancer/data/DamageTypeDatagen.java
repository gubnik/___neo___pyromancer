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
package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.concurrent.CompletableFuture;

public class DamageTypeDatagen extends TagsProvider<DamageType>
{
    public static final ResourceKey<DamageType> FLAME_KEY = register("flame");
    public static final ResourceKey<DamageType> HELLBLAZE_KEY = register("hellblaze");
    public static final ResourceKey<DamageType> SOULFLAME_KEY = register("soulflame");

    public static final ResourceKey<DamageType> BOMBSACK_KEY = register("bombsack");
    public static final ResourceKey<DamageType> HOGTRAP_KEY = register("hogtrap");

    public static final ResourceKey<DamageType> SIZZLING_HAND_KEY = register("sizzling_hand");
    public static final ResourceKey<DamageType> COURT_OF_EMBERS_KEY = register("court_of_embers");
    public static final ResourceKey<DamageType> SYMBOL_OF_SUN_KEY = register("symbol_of_sun");
    public static final ResourceKey<DamageType> FLAMMENKLINGE_LAUNCH_KEY = register("flammenklinge_launch");
    public static final ResourceKey<DamageType> FLAMMENKLINGE_LAND_KEY = register("flammenklinge_land");

    public static final ResourceKey<DamageType> FIREBRIAR_KEY = register("firebriar");
    public static final ResourceKey<DamageType> BLAZING_JOURNAL_PROJECTION_KEY = register("blazing_journal_projection");
    public static final ResourceKey<DamageType> GUILLOTINE_KEY = register("guillotine");
    public static final ResourceKey<DamageType> UNBURNED_KEY = register("unburned");

    public static final ResourceKey<DamageType> HOARFROST_GREATSWORD_KEY = register("hoarfrost_greatsword");
    public static final ResourceKey<DamageType> SPEAR_OF_MOROZ_KEY = register("spear_of_moroz");
    public static final ResourceKey<DamageType> FROSTCOPPER_GOLEM_STOMP_KEY = register("frostcopper_golem_stomp");

    public static final ResourceKey<DamageType> MUSKET_SHOT_KEY = register("musket_shot");

    public static final ResourceKey<DamageType> VAPORIZER_GENERIC_KEY = register("vaporizer_generic");

    public static final DamageType MUSKET_SHOT = new DamageType(MUSKET_SHOT_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f);
    public static final DamageType FLAME = new DamageType(FLAME_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType HELLBLAZE = new DamageType(HELLBLAZE_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType SOULFLAME = new DamageType(SOULFLAME_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType BOMBSACK = new DamageType(BOMBSACK_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType HOGTRAP = new DamageType(HOGTRAP_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType SIZZLING_HAND = new DamageType(SIZZLING_HAND_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType COURT_OF_EMBERS = new DamageType(COURT_OF_EMBERS_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType SYMBOL_OF_SUN = new DamageType(SYMBOL_OF_SUN_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType FLAMMENKLINGE_LAUNCH = new DamageType(FLAMMENKLINGE_LAUNCH_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType FLAMMENKLINGE_LAND = new DamageType(FLAMMENKLINGE_LAND_KEY.location().getPath(), DamageScaling.NEVER, 0.1f);
    public static final DamageType FIREBRIAR = new DamageType(FIREBRIAR_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f);
    public static final DamageType BLAZING_JOURNAL_PROJECTION = new DamageType(BLAZING_JOURNAL_PROJECTION_KEY.location().getPath(), DamageScaling.NEVER, 0);
    public static final DamageType GUILLOTINE = new DamageType(GUILLOTINE_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0);
    public static final DamageType UNBURNED = new DamageType(UNBURNED_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0);
    public static final DamageType HOARFROST_GREATSWORD = new DamageType(HOARFROST_GREATSWORD_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f);
    public static final DamageType SPEAR_OF_MOROZ = new DamageType(SPEAR_OF_MOROZ_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f);
    public static final DamageType FROSTCOPPER_GOLEM_STOMP = new DamageType(FROSTCOPPER_GOLEM_STOMP_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.2f);
    public static final DamageType VAPORIZER_GENERIC = new DamageType(VAPORIZER_GENERIC_KEY.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f);

    /**
     * Tag for damage types that are caused by Embers
     */
    public static TagKey<DamageType> IS_EMBER = create("is_ember");

    /**
     * Tag for damage types that are brutish (bombsacks, mercury traps, rot etc.)
     */
    public static TagKey<DamageType> IS_BRUTISH = create("is_brutish");

    /**
     * Tag for pyromancies damage
     */
    public static TagKey<DamageType> IS_PYROMANCY = create("is_pyromancy");

    /**
     * Tag for damage types that are caused by Blazing Journal's enchantments' attacks
     */
    public static TagKey<DamageType> JOURNAL_PROJECTION = create("journal_projection");

    /**
     * Tag for pyromancies damage
     */
    public static TagKey<DamageType> IS_VAPORIZER = create("is_vaporizer");

    public DamageTypeDatagen (PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, PyromancerMod.MOD_ID, existingFileHelper);
    }

    public static ResourceKey<DamageType> register (String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }

    public static TagKey<DamageType> create (String name)
    {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }

    public static void generate (BootstapContext<DamageType> bootstrap)
    {
        bootstrap.register(FLAME_KEY, FLAME);
        bootstrap.register(HELLBLAZE_KEY, HELLBLAZE);
        bootstrap.register(SOULFLAME_KEY, SOULFLAME);
        bootstrap.register(BOMBSACK_KEY, BOMBSACK);
        bootstrap.register(HOGTRAP_KEY, HOGTRAP);
        bootstrap.register(SIZZLING_HAND_KEY, SIZZLING_HAND);
        bootstrap.register(COURT_OF_EMBERS_KEY, COURT_OF_EMBERS);
        bootstrap.register(SYMBOL_OF_SUN_KEY, SYMBOL_OF_SUN);
        bootstrap.register(FLAMMENKLINGE_LAUNCH_KEY, FLAMMENKLINGE_LAUNCH);
        bootstrap.register(FLAMMENKLINGE_LAND_KEY, FLAMMENKLINGE_LAND);
        bootstrap.register(FIREBRIAR_KEY, FIREBRIAR);
        bootstrap.register(BLAZING_JOURNAL_PROJECTION_KEY, BLAZING_JOURNAL_PROJECTION);
        bootstrap.register(UNBURNED_KEY, UNBURNED);
        bootstrap.register(HOARFROST_GREATSWORD_KEY, HOARFROST_GREATSWORD);
        bootstrap.register(SPEAR_OF_MOROZ_KEY, SPEAR_OF_MOROZ);
        bootstrap.register(FROSTCOPPER_GOLEM_STOMP_KEY, FROSTCOPPER_GOLEM_STOMP);
        bootstrap.register(MUSKET_SHOT_KEY, MUSKET_SHOT);
        bootstrap.register(GUILLOTINE_KEY, GUILLOTINE);
        bootstrap.register(VAPORIZER_GENERIC_KEY, VAPORIZER_GENERIC);
    }

    @Override
    protected void addTags (HolderLookup.@NotNull Provider p_256380_)
    {
        tag(IS_EMBER)
                .add(FLAME_KEY)
                .add(HELLBLAZE_KEY)
                .add(SOULFLAME_KEY);
        tag(IS_PYROMANCY)
                .add(SIZZLING_HAND_KEY)
                .add(COURT_OF_EMBERS_KEY)
                .add(SYMBOL_OF_SUN_KEY)
                .add(FLAMMENKLINGE_LAUNCH_KEY)
                .add(FLAMMENKLINGE_LAND_KEY);
        tag(IS_BRUTISH)
                .add(BOMBSACK_KEY)
                .add(HOGTRAP_KEY);
        tag(JOURNAL_PROJECTION)
                .add(BLAZING_JOURNAL_PROJECTION_KEY);
        tag(IS_VAPORIZER)
                .add(
                        VAPORIZER_GENERIC_KEY
                );
        tag(DamageTypeTags.IS_PROJECTILE)
                .add(
                        SIZZLING_HAND_KEY,
                        BOMBSACK_KEY,
                        COURT_OF_EMBERS_KEY,
                        MUSKET_SHOT_KEY
                );
        tag(DamageTypeTags.IS_FIRE)
                .addTag(IS_EMBER)
                .addTag(JOURNAL_PROJECTION)
                .addTag(IS_PYROMANCY)
                .add(FIREBRIAR_KEY)
                .add(BLAZING_JOURNAL_PROJECTION_KEY)
                .add(SOULFLAME_KEY)
                .add(GUILLOTINE_KEY);
    }
}

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.ContractRegistry;
import xyz.nikgub.pyromancer.registry.EntityTypeRegistry;

import java.util.concurrent.CompletableFuture;

public class EntityTagDatagen extends ForgeEntityTypeTagsProvider
{
    public static final TagKey<EntityType<?>> FLAMING_GROVE_NATIVE = create("flaming_grove_native");
    public static final TagKey<EntityType<?>> CONTRACT_SUMMONED = create("can_be_contract_summoned");

    public EntityTagDatagen (PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, existingFileHelper);
    }

    public static TagKey<EntityType<?>> create (String tagName)
    {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(PyromancerMod.MOD_ID, tagName));
    }

    @Override
    public void addTags (HolderLookup.@NotNull Provider lookupProvider)
    {
        tag(FLAMING_GROVE_NATIVE)
            .add(EntityTypeRegistry.SCORCH.get(), EntityTypeRegistry.PYRACORN.get(), EntityTypeRegistry.UNBURNED.get());
        for (var entityType : ContractRegistry.CONTRACTS.getEntries().stream().map(e -> e.get().getEntityToSummon()).toArray(EntityType[]::new))
        {
            tag(CONTRACT_SUMMONED)
                .add(entityType);
        }
    }
}

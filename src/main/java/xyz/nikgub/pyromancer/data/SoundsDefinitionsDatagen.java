package xyz.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.SoundEventRegistry;

public class SoundsDefinitionsDatagen extends SoundDefinitionsProvider
{

    public SoundsDefinitionsDatagen (PackOutput output, ExistingFileHelper helper)
    {
        super(output, PyromancerMod.MOD_ID, helper);
    }

    @Override
    public void registerSounds ()
    {
        this.add(SoundEventRegistry.MUSKET_SHOT.getId(), SoundDefinition.definition().subtitle("pyromancer.subtitle.musket_shot").replace(true).with(sound("pyromancer:musket_shot")));
        this.add(SoundEventRegistry.MUSKET_LOAD.getId(), SoundDefinition.definition().subtitle("pyromancer.subtitle.musket_load").replace(true).with(sound("pyromancer:musket_load")));
    }
}

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
package xyz.nikgub.pyromancer.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MercuryVaporParticle extends TextureSheetParticle
{
    private final SpriteSet spriteSet;

    protected MercuryVaporParticle (ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet)
    {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 3f;
        this.lifetime = 7;
        this.gravity = -0.1f;
        this.hasPhysics = false;
        this.xd = vx * 0;
        this.yd = vy * 0;
        this.zd = vz * 0;
        this.setSpriteFromAge(spriteSet);
    }

    public static Provider provider (SpriteSet spriteSet)
    {
        return new Provider(spriteSet);
    }

    public static ParticleProvider<SimpleParticleType> vaporizerProvider (SpriteSet spriteSet)
    {
        return new VaporizerProvider(spriteSet);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType ()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick ()
    {
        super.tick();
        if (!this.removed)
        {
            this.setSprite(this.spriteSet.get((this.age / 2) % 8 + 1, 8));
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Provider (SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle (@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new MercuryVaporParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    public static class VaporizerProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public VaporizerProvider (SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle (@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new MercuryVaporParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}

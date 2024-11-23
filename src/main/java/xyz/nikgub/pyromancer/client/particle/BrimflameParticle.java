package xyz.nikgub.pyromancer.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BrimflameParticle extends TextureSheetParticle
{
    protected BrimflameParticle (ClientLevel world, double x, double y, double z, double vx, double vy, double vz)
    {
        super(world, x, y, z);
        this.setSize(0.1f, 0.1f);
        this.quadSize *= 0.5f;
        this.lifetime = 7;
        this.gravity = -0.2f;
        this.hasPhysics = true;
        this.xd = vx * 0;
        this.yd = vy * 0;
        this.zd = vz * 0;
    }

    public static BrimflameParticle.Provider provider (SpriteSet spriteSet)
    {
        return new BrimflameParticle.Provider(spriteSet);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType ()
    {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick ()
    {
        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public Provider (SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle (@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed)
        {
            BrimflameParticle particle = new BrimflameParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}

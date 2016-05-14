package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alex on 14/05/16.
 */
public class ParticleManager {

    private static ParticleEffectPool pool;
    private static Array<ParticleEffectPool.PooledEffect> activeEffects;
    private static FrameBuffer particleBuffer;
    private static TextureRegion particleRegion;

    public static void initialise() {
        particleRegion = new TextureRegion();
        particleBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        ParticleEffect explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal("explosion.particle"), Gdx.files.internal(""));
        pool = new ParticleEffectPool(explosionEffect, 10, 100);
        activeEffects = new Array<ParticleEffectPool.PooledEffect>();
    }

    public static void add(float x, float y) {
        ParticleEffectPool.PooledEffect effect = pool.obtain();

        activeEffects.add(effect);
        effect.setPosition(x,y);
    }

    public static void render(SpriteBatch batch) {
        for (int i = 0; i < activeEffects.size; ) {
            ParticleEffectPool.PooledEffect effect = activeEffects.get(i);

            if (effect.isComplete()) {
                pool.free(effect);
                activeEffects.removeIndex(i);
            }
            else {
                effect.draw(batch, Gdx.graphics.getDeltaTime());
                ++i;
            }
        }

        particleRegion.setRegion(particleBuffer.getColorBufferTexture());
        particleRegion.flip(false, true);
    }

    public static void render2(SpriteBatch batch) {
        batch.draw(particleRegion,
                0.0f, 0.0f,
                0.0f, 0.0f,
                particleRegion.getRegionWidth(), particleRegion.getRegionHeight(),
                Game.WORLD_TO_SCREEN, Game.WORLD_TO_SCREEN,
                0.0f);
    }

    public static void bind() {
        particleBuffer.bind();
    }
}

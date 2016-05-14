package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alex on 03/05/16.
 */
public abstract class XSprite extends Sprite {

    public XSprite(Texture texture) {
        this(new TextureRegion(texture));
    }

    public XSprite(TextureRegion textureRegion) {
        super(textureRegion);
        setScale(Game.WORLD_TO_SCREEN);
        setOrigin(getWidth()/2,getHeight()/2);// * 0.5f, getHeight() * 0.5f);
    }

    public float getWidth() {
        return super.getWidth() * Game.WORLD_TO_SCREEN;
    }

    public float getHeight() {
        return super.getHeight() * Game.WORLD_TO_SCREEN;
    }

    public float getCentreX() {
        return getX() + getWidth()/2;
    }

    public float getCentreY() {
        return getY() + getHeight()/2;
    }
}

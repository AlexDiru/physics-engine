package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by alex on 06/05/16.
 */
public class Player extends XSpriteBox2D {


    public Player(World world, TextureRegion textureRegion, float density) {
        super(world, textureRegion, density, BodyDef.BodyType.DynamicBody, BodyShape.Circle);
        body.setAngularDamping(999999);
        //body.setFixedRotation(true);
    }

    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch, false);
    }
}

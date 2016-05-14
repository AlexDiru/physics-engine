package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by alex on 04/05/16.
 */
public class XSpriteBox2D extends XSprite {

    private float BOX_2D_SCALE_FACTOR = 0.5f; //It just works
    protected Body body;
    private float moveSpeed = 10f;

    protected float bodyWidth;
    protected float bodyHeight;

    public void handleInput() {
        Vector2 vel = body.getLinearVelocity();
        boolean keyPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            vel.y = moveSpeed;
            keyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            vel.y = -moveSpeed;
            keyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            vel.x = moveSpeed;
            keyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            vel.x = -moveSpeed;
            keyPressed = true;
        }

        if (!keyPressed) {
            vel.x = vel.y = 0;
        }

        body.setLinearVelocity(vel);
    }


    public XSpriteBox2D(World world, TextureRegion textureRegion, float density) {
        this(world, textureRegion, density, BodyDef.BodyType.DynamicBody, BodyShape.Square);
    }

    public XSpriteBox2D(World world, TextureRegion textureRegion, float density, BodyDef.BodyType bodyType,BodyShape bodyShape) {
        this(world, textureRegion, density, 80,0, 0,0, 21 * Game.WORLD_TO_SCREEN,21 * Game.WORLD_TO_SCREEN, bodyType, bodyShape);
    }

    public enum BodyShape {
        Square,
        Circle
    }

    public XSpriteBox2D(World world, TextureRegion textureRegion, float density, float friction, float restitution, float xPos, float yPos, float width, float height, BodyDef.BodyType bodyType, BodyShape bodyShape) {
        super(textureRegion);

        bodyWidth = width * BOX_2D_SCALE_FACTOR;
        bodyHeight = height * BOX_2D_SCALE_FACTOR;

        Shape boxShape;

        if (bodyShape == BodyShape.Square) {
            boxShape = new PolygonShape();
            ((PolygonShape)boxShape).setAsBox(bodyWidth, bodyHeight);
        }
        else {
            boxShape = new CircleShape();
            boxShape.setRadius(bodyWidth);
        }
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxShape;
        boxFixtureDef.density = density;
        boxFixtureDef.friction = 80f;
        boxFixtureDef.restitution = 0f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;

        body = world.createBody(bodyDef);
        body.createFixture(boxFixtureDef);
        body.setLinearDamping(99);
        body.setAngularDamping(99);
        body.setTransform(xPos ,yPos,0);
        body.setBullet(true);
    }

    public void draw(SpriteBatch spriteBatch) {
        draw(spriteBatch, true);
    }

    public void draw(SpriteBatch spriteBatch, boolean drawRotation) {
        Vector2 bodyOrigin = new Vector2(bodyWidth, bodyHeight); //This seems to be in pixel size
        Vector2 bottlePos = body.getPosition().sub(bodyOrigin.scl(1 / Game.WORLD_TO_SCREEN));

        super.setPosition(bottlePos.x, bottlePos.y);
        super.setOrigin(bodyOrigin.x, bodyOrigin.y);
        if (drawRotation)
            super.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        super.draw(spriteBatch);
    }

    public void setPosition(float x, float y) {
        //super.setPosition(x,y);
        body.setTransform(x, y,0);
    }

    public Body getBody() {
        return body;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }
}

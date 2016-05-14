package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class MapBodyBuilderXSpriteBox2D {

    //Custom Properties Available
    //friction
    //density



    //Static variables to reduce function parameter number
    private static BodyDef.BodyType bodyType;
    private static String layerName;
    private static World world;
    private static Map map;

    private static Array<XSpriteBox2D> buildObjects() {
        Array<XSpriteBox2D> bodies = new Array<XSpriteBox2D>();

        MapObjects objects = map.getLayers().get(layerName).getObjects();

        for(MapObject object : objects) {
            if (object instanceof TextureMapObject)
                bodies.add(getRectangle(world, (TextureMapObject) object));
            else
                System.out.println(object);
        }

        return bodies;
    }

    private static Array<XSpriteBox2D> buildTiles() {
        Array<XSpriteBox2D> bodies = new Array<XSpriteBox2D>();
        TiledMapTileLayer layer = null;

        try {
            layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        } catch (ClassCastException e) {
            return bodies;
        }

        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if (cell != null && cell.getTile() != null) {
                    bodies.add(getRectangle(cell.getTile().getTextureRegion(), (x+0.5f) * Game.WORLD_TO_SCREEN * 16, (y+0.5f) * Game.WORLD_TO_SCREEN * 16,
                            (float)cell.getTile().getTextureRegion().getRegionWidth() * Game.WORLD_TO_SCREEN,
                            (float)cell.getTile().getTextureRegion().getRegionHeight() * Game.WORLD_TO_SCREEN));
                }
            }

        return bodies;
    }

    public static Array<XSpriteBox2D> buildShapes(Map map, World world, String layerName, BodyDef.BodyType bodyType) {
        //Init statics
        MapBodyBuilderXSpriteBox2D.bodyType = bodyType;
        MapBodyBuilderXSpriteBox2D.layerName = layerName;
        MapBodyBuilderXSpriteBox2D.world = world;
        MapBodyBuilderXSpriteBox2D.map = map;

        Array<XSpriteBox2D> bodies = new Array<XSpriteBox2D>();

        bodies.addAll(MapBodyBuilderXSpriteBox2D.buildObjects());
        bodies.addAll(MapBodyBuilderXSpriteBox2D.buildTiles());

        return bodies;
    }

    private static XSpriteBox2D getRectangle(TextureRegion textureRegion, float x, float y, float width, float height) {
        XSpriteBox2D xs = new XSpriteBox2D(world, textureRegion, 80f, 0.2f, 0f, x, y, width, height, bodyType, XSpriteBox2D.BodyShape.Square);
        return xs;
    }

    private static XSpriteBox2D getRectangle(World world, TextureMapObject rectangleObject) {
        float xPos = rectangleObject.getX() * Game.WORLD_TO_SCREEN;
        float yPos = rectangleObject.getY() * Game.WORLD_TO_SCREEN;
        float width = rectangleObject.getTextureRegion().getRegionWidth() * Game.WORLD_TO_SCREEN;
        float height = rectangleObject.getTextureRegion().getRegionHeight() * Game.WORLD_TO_SCREEN;

        float friction = 80f;
        float density = 0.2f;
        float restitution = 0f;

        if (rectangleObject.getProperties().containsKey("friction"))
            friction = Float.parseFloat((String)rectangleObject.getProperties().get("friction"));
        if (rectangleObject.getProperties().containsKey("density"))
            density = Float.parseFloat((String) rectangleObject.getProperties().get("density"));
        if (rectangleObject.getProperties().containsKey("restitution"))
            restitution = Float.parseFloat((String) rectangleObject.getProperties().get("restitution"));

        XSpriteBox2D xs = new XSpriteBox2D(world, rectangleObject.getTextureRegion(), density, friction, restitution, xPos, yPos, width, height, MapBodyBuilderXSpriteBox2D.bodyType, XSpriteBox2D.BodyShape.Square);

        Gdx.app.log("Adding TextureMapObject As Body",
                    "Pos = (" + xPos + ", " + yPos + ") ; " +
                    "Size = (" + width + ", " + height + ")");
        return xs;
    }

}
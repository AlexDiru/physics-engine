package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

public class Game extends InputAdapter implements ApplicationListener {

	//Box = 21px = 32px (round up)
	//Boxes per width = 40m
	//Boxes per height = 24m


	private static Color BACKGROUND_COLOR = new Color(0.39f,0.58f,0.92f,1f);
	private static float SCENE_WIDTH = 40f;
	private static float SCENE_HEIGHT = 24f;
	public static float WORLD_TO_SCREEN = SCENE_WIDTH/1280f;

	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;

	private List<XSpriteBox2D> backgroundSprites;
	private Player player;
	private XSpriteBox2D chest;

	private TiledMap map;
	private TmxMapLoader loader;
	private OrthogonalTiledMapRenderer renderer;
	private Array<XSpriteBox2D> mapBodies = new Array<XSpriteBox2D>();

	//Touch Joystick controls
	private Stage stage;
	private Touchpad touchpad;
	private Touchpad.TouchpadStyle touchpadStyle;
	private Skin touchpadSkin;
	private Drawable touchBackground;
	private Drawable touchKnob;

	private World world;
	private Box2DDebugRenderer debugRenderer;

	private void loadMap() {
		world = new World(new Vector2(0f,0f), false); //Loads box2D

		debugRenderer = new Box2DDebugRenderer();

		loader = new TmxMapLoader();
		map = loader.load("map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, WORLD_TO_SCREEN);

		if (mapBodies != null)
			mapBodies.clear();

		mapBodies = new Array<XSpriteBox2D>();

		mapBodies.addAll(MapBodyBuilderXSpriteBox2D.buildShapes(map, world, "Obstacles", BodyDef.BodyType.DynamicBody));
		mapBodies.addAll(MapBodyBuilderXSpriteBox2D.buildShapes(map, world, "FixedObstacles", BodyDef.BodyType.StaticBody));
		mapBodies.addAll(MapBodyBuilderXSpriteBox2D.buildShapes(map, world, "FixedTiles", BodyDef.BodyType.DynamicBody));
		map.getLayers().remove(map.getLayers().get("FixedTiles")); //don't render tiles twice

		player = new Player(world, new TextureRegion(new Texture(Gdx.files.internal("player.png"))), 80f);
		player.setPosition(0,0);

		camera.position.set(player.getX(), player.getY(), 0);
	}

	@Override
	public void dispose(){

	}

	@Override
	public void resume(){

	}
	@Override
	public void pause(){

	}
	@Override
	public void resize(int x, int y){
		viewport.update(x,y);
	}

	private void setupTouchpad() {
		//Create a touchpad skin
		touchpadSkin = new Skin();
		//Set background image
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("touchBackground.png")));
		//Set knob image
		touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("touchKnob.png")));
		//Create TouchPad Style
		touchpadStyle = new Touchpad.TouchpadStyle();
		//Create Drawable's from TouchPad skin
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		//Apply the Drawables to the TouchPad Style
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		//Create new TouchPad with the created style
		touchpad = new Touchpad(10, touchpadStyle);
		//setBounds(x,y,width,height)
		touchpad.setBounds(15, 15, 200, 200);


		//stage = new Stage();
		//stage.addActor(touchpad);
		//Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void create () {


		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		viewport.apply();

		loadMap();

		//world.setContinuousPhysics(true);

		ParticleManager.initialise();


		batch = new SpriteBatch();


		//setupTouchpad();

		//Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		double angle = Math.atan2(Gdx.graphics.getHeight() - screenY - player.getCentreY(), screenX - player.getCentreX());
		player.setRotation((float)(angle*(180f/Math.PI)) + 270f);

		return true;
	}

	private boolean keyFlagR = false;
	private boolean keyFlagT = false;
	private boolean keyFlagP = false;
	private boolean viewDebugRenderer = true;

	@Override
	public void render () {

		world.step(1f/60f, 60,20);

		if (Gdx.input.isKeyPressed(Input.Keys.R) && !keyFlagR) {
			keyFlagR = true;
			loadMap();
		} else if (!Gdx.input.isKeyPressed(Input.Keys.R)) {
			keyFlagR = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.T) && !keyFlagT) {
			keyFlagT = true;
			viewDebugRenderer = !viewDebugRenderer;
		} else if (!Gdx.input.isKeyPressed(Input.Keys.T)) {
			keyFlagT = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.P) && !keyFlagP) {
			keyFlagP = true;
			System.out.println("Adding Particle");
			ParticleManager.add(player.getX(), player.getY());
		} else if (!Gdx.input.isKeyPressed(Input.Keys.P)) {
			keyFlagP = false;
		}

		player.getBody().getFixtureList().get(0).setFriction(10f);
		player.handleInput();
		player.getBody().setBullet(true);
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.setView((OrthographicCamera)viewport.getCamera());
		renderer.render();

		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();

		player.draw(batch);

		for (XSpriteBox2D body : mapBodies)
			body.draw(batch);

		ParticleManager.render(batch);

		batch.end();

		if (viewDebugRenderer)
			debugRenderer.render(world, viewport.getCamera().combined);
	}
}

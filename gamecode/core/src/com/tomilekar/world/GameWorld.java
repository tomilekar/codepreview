package com.tomilekar.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.tomilekar.MainScreen;

public class GameWorld implements ContactListener {
    public World getWorld() {
        return world;
    }

    public class WorldVars {
        public final static short PLAYER = 2;
        public final static short BUILDINGS = 4;
        public final static float GRAVITY_Y = -9.1f;
        public final static float GRAVITY_X = 0f;
        public static final String MAP_1 = "maps/map_1.tmx";
        public static final float  WORLD_BOUND_X = 0f;
        public static final float PLAYER_SPEED_X = 2f;
        public static final float PLAYER_SPEED_Y = 2f;

    }


    private MainScreen mainScreen;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Array<Body> bodyArray;
    private Array<Fixture> fixtures;

    public  TmxMapLoader tmxMapLoader;
    private TiledMap map;
    public static IsometricTiledMapRenderer mapRenderer;

    public GameWorld(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        this.tmxMapLoader = new TmxMapLoader();

    }

    public void init() {
        map = tmxMapLoader.load(WorldVars.MAP_1);

        initWorld();
        initMap();

    }

    private void initMap() {

        mapRenderer = new IsometricTiledMapRenderer(map);

        box2DDebugRenderer.render(world, mainScreen.orthographicCamera.combined);
    }

    private void initWorld() {
        Box2D.init();

        bodyArray = new Array<>();
        fixtures = new Array<>();

        world = new World(new Vector2(WorldVars.GRAVITY_X, WorldVars.GRAVITY_Y), true);
        world.setContactListener(this);
        world.step(Gdx.graphics.getDeltaTime(), 2, 2);
        box2DDebugRenderer = new Box2DDebugRenderer();

        for (MapLayer mapLayer : map.getLayers()) {
            System.out.println(mapLayer.getName());
            for (MapObject mapObject : mapLayer.getObjects()) {
                float X = (float) mapObject.getProperties().get("x");
                float Y = (float) mapObject.getProperties().get("y");
                System.out.println(" X: " + X + " Y: " + Y);
                System.out.println(Y);
                BodyDef bodyDef = new BodyDef();
                bodyDef.active = true;
                bodyDef.type = BodyDef.BodyType.KinematicBody;
                bodyDef.allowSleep = true;
                bodyDef.position.x = X + 40f*3f;
                bodyDef.position.y = Y - Y / 1.2f;
                System.out.println("Position X:  " + bodyDef.position.x + "\nPosition Y: " + bodyDef.position.y);

                PolygonShape polygonShape = new PolygonShape();
                polygonShape.setAsBox(X / 24f, Y / 24f);
                System.out.println("Polygonshape X:  " + X / 2f + "\nPolygsonshape Y: " + Y / 2f);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = polygonShape;
                fixtureDef.isSensor = true;

                fixtureDef.filter.maskBits = GameWorld.WorldVars.BUILDINGS;
                Body body = world.createBody(bodyDef);
                Fixture fixture = body.createFixture(fixtureDef);
                fixture.setUserData("Test1");
                bodyArray.add(body);
                fixtures.add(body.createFixture(fixtureDef));


            }

        }

    }

    public void render(float timeDelta) {
        mapRenderer.setView(mainScreen.orthographicCamera);
        mapRenderer.render();
        box2DDebugRenderer.render(world, mainScreen.orthographicCamera.combined);
    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

            Gdx.app.log("Contact","1");


            Gdx.app.log("Contact","2");


}

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}

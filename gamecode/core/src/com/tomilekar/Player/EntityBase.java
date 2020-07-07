package com.tomilekar.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.tomilekar.MainScreen;
import com.tomilekar.world.GameWorld;


public class EntityBase {

    // Units
    public Vector2 position = new Vector2(150f,0f);
    private Vector2 gravity = new Vector2(0f,-9.81f);


    // World vars
    private short maskBit;
    private GameWorld world;
    private Body body;
    private BodyDef bodyDef;
    private Fixture fixture;
    private FixtureDef fixtureDef;
    private MainScreen mainScreen;
    private Texture texture;

    private EntityAnimation entityAnimation;


    private Shape shape;
    public float BOUND_X = 10f;
    public float BOUND_Y = 10f;
    public GameObject gameObject;
    private boolean canMove;

    public EntityBase(MainScreen mainScreen, GameWorld world, Texture texuture){
        this(mainScreen,world,texuture,GameObject.STANDARD);
    }


    public EntityBase(MainScreen mainScreen, GameWorld world, Texture texuture, boolean canMove){
        this(mainScreen,world,texuture,GameObject.STANDARD, canMove);
    }

    public EntityBase(MainScreen mainScreen, GameWorld world, Texture texuture, GameObject gameObject, boolean canMove){
        this.world = world;
        this.mainScreen = mainScreen;
        this.texture = texuture;
        this.gameObject = gameObject;
        this.canMove = canMove;
    }

    public EntityBase(MainScreen mainScreen, GameWorld world, Texture texuture, GameObject gameObject){
        this.world = world;
        this.mainScreen = mainScreen;
        this.texture = texuture;
        this.gameObject = gameObject;
    }

    private void initAnimation (){
        this.entityAnimation = new EntityAnimation(EntityAnimation.Direction.values(), EntityAnimation.Anim.values(), EntityAnimation.Entity.charakter,EntityAnimation.Player.values());
        this.entityAnimation.initAnimations();
        this.entityAnimation.debugDirectionAnimation();
    }

    public void initEntity(){
        initWorld();
        initAnimation();
    }
    public void draw(float deltaTime){
        mainScreen.spriteBatch.draw(texture,position.x,position.y);
    }
    private void initWorld(){
        if(texture != null){
            this.BOUND_X = texture.getWidth() / 2f;
            this.BOUND_Y = texture.getHeight() / 2f;
        }
        if(this.gameObject == null){
            this.gameObject = GameObject.STANDARD;
        }
        this.shape = new PolygonShape();
        PolygonShape polygonShape = (PolygonShape) this.shape;
        polygonShape.setAsBox(BOUND_X, BOUND_Y);
        this.shape = polygonShape;

        this.bodyDef =  new BodyDef();
        bodyDef.position.x = position.x + BOUND_X;
        bodyDef.position.y = position.y + BOUND_Y;
        bodyDef.active = true;
        bodyDef.allowSleep = true;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        if(canMove){
            bodyDef.type = BodyDef.BodyType.DynamicBody;

        }
        this.fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = true;
        //reibung
        fixtureDef.friction = 2f;
        //widerstand - abprall
        fixtureDef.restitution = 2f;
        //dichte
        fixtureDef.density = 2f;
        fixtureDef.filter.maskBits = this.gameObject.getMaskBit();
        fixtureDef.filter.categoryBits = this.gameObject.getCategoryBit();
        fixtureDef.filter.groupIndex = this.gameObject.getGroupIndex();

        this.body = world.getWorld().createBody(bodyDef);
        this.fixture = body.createFixture(fixtureDef);
        this.body.setUserData(gameObject.getLabel());


    }

    public EntityAnimation getEntityAnimation() {
        return entityAnimation;
    }

    public void setEntityAnimation(EntityAnimation entityAnimation) {
        this.entityAnimation = entityAnimation;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public void dispose(){

        texture.dispose();
        world.getWorld().destroyBody(body);
    }
}

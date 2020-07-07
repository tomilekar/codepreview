package com.tomilekar.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.tomilekar.AnimationC.AnimationSpriteSorter;
import com.tomilekar.MainScreen;
import com.tomilekar.world.GameWorld;


public class Monster  {

    MainScreen mainScreen;
    private Body body;
    private World world;
    private Vector2 position = new Vector2(400f,400f);
    private Vector2 speed = new Vector2(4f,3f);
    private Fixture fixture;
    private PolygonShape polygonShape;
    private Texture texture;



    private Sprite sprite;
    private TextureRegion textureRegion;
    Animation animation;

    boolean isRight = true;
    boolean isUp = false;


    private AnimationSpriteSorter.Direction lastDirection = AnimationSpriteSorter.Direction.right;



    public Monster(World world, MainScreen mainScreen) {

        this.world = world;
        this.mainScreen = mainScreen;

    }

    public void init(AnimationSpriteSorter.Directory directory){
        initAnim(directory);
        initWorld();


    }
    private void initAnim(AnimationSpriteSorter.Directory directory){
       // animation = new Animation(directory);
        //animation.init();
//        textureRegion = animation.witch_idle.getKeyFrame(Gdx.graphics.getDeltaTime());
        texture = new Texture(Gdx.files.internal("char_1.png"));
        sprite = new Sprite(texture);
        sprite.setPosition(position.x, position.y);


    }

    public void update(float deltaTime, SpriteBatch spriteBatch){
//        textureRegion = animation.witch_idle.getKeyFrame(deltaTime);
        sprite.setRegion(texture);
        sprite.draw(spriteBatch);



    }


    private void initWorld(){


        polygonShape = new PolygonShape();
        polygonShape.setAsBox(texture.getHeight()/2f, texture.getWidth()/2f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.x = position.x + texture.getWidth() /2f ;
        bodyDef.position.y = position.y + texture.getHeight() /2f;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.restitution = 0;
        fixtureDef.filter.maskBits = GameWorld.WorldVars.PLAYER;
        fixtureDef.isSensor = true;
        body = world.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Test");
    }


    void updatePosition(){
        sprite.setPosition(position.x, position.y);
        body.setTransform(position.x + textureRegion.getRegionWidth() /2f, position.y + textureRegion.getRegionHeight() / 2f, 0);

    }
    void move(float deltaTime) {


        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            lastDirection = AnimationSpriteSorter.Direction.right;
            isUp = false;
            isRight = true;
            textureRegion = animation.run_right.getKeyFrame(deltaTime);
            position.x += speed.x;
            mainScreen.orthographicCamera.position.x += speed.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            lastDirection = AnimationSpriteSorter.Direction.left;
            isUp = false;
            isRight = false;
            textureRegion = animation.run_left.getKeyFrame(deltaTime);
            position.x -= speed.x;
            mainScreen.orthographicCamera.position.x -= speed.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            lastDirection = AnimationSpriteSorter.Direction.down;
            isUp = false;
            isRight = false;
            textureRegion = animation.run_down.getKeyFrame(deltaTime);
            position.y -= speed.y;

            mainScreen.orthographicCamera.position.y -= speed.y;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            lastDirection = AnimationSpriteSorter.Direction.up;
            isUp = true;
            isRight = false;
            textureRegion = animation.run_up.getKeyFrame(deltaTime);
            position.y += speed.y;
            mainScreen.orthographicCamera.position.y += speed.y;

        } else {
            if (lastDirection.equals(AnimationSpriteSorter.Direction.right)) {
                textureRegion = animation.idle_right.getKeyFrame(deltaTime);
            }
            if (lastDirection.equals(AnimationSpriteSorter.Direction.left)) {
                textureRegion = animation.idle_left.getKeyFrame(deltaTime);
            }
            if (lastDirection.equals(AnimationSpriteSorter.Direction.down)) {
                textureRegion = animation.idle_down.getKeyFrame(deltaTime);
            }
            if (lastDirection.equals(AnimationSpriteSorter.Direction.up)) {
                textureRegion = animation.idle_up.getKeyFrame(deltaTime);
            }
        }
        updatePosition();


    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Sprite getSprite() {
        return sprite;
    }
}

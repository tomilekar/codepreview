package com.tomilekar.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.tomilekar.MainScreen;
import com.tomilekar.world.GameWorld;

import java.util.HashMap;

public class Player_ extends MovingObject {


    public String name;
    private float animationCounter = 0f;
    private Enum lastAnimation  = EntityAnimation.Anim.idle;
    private Enum lastDirection = EntityAnimation.Direction.right;

    @Override
    public void startAnimation(Enum animation, Enum direction,float deltaTime) {
        animationCounter += 1/60f;
        if(animationCounter > 5f){
            animationCounter = 0f;
        }
        TextureRegion textureRegion = null;

        if (this.lastAnimation == null && this.lastDirection == null) {
            Gdx.app.log("Player : " + name, " animation = null & direction = null");
                textureRegion =  this.getEntityAnimation().getDirectionAnimationsMap().get(EntityAnimation.Anim.idle).get(EntityAnimation.Direction.right).getKeyFrame(deltaTime);;
            return;
        }
        textureRegion = this.getEntityAnimation().getDirectionAnimationsMap().get(this.lastAnimation).get(this.lastDirection).getKeyFrame(deltaTime);

        this.getSprite().setRegion(textureRegion);

    }

    public Player_(MainScreen mainScreen, GameWorld world, Texture texuture, String name) {
        super(mainScreen, world, texuture, GameObject.PlAYER);
        this.name = name;
    }

    @Override
    public void draw(float deltaTime) {

        this.animationCounter += deltaTime;
        if(animationCounter > 5f){
            animationCounter = 0f;
        }
        updatePosition(deltaTime);

        startAnimation(this.lastAnimation, this.lastDirection,deltaTime);
        this.getSprite().draw(this.getMainScreen().spriteBatch);
        this.getSprite().setPosition(position.x, position.y);
        this.getBody().setTransform(BOUND_X + position.x, BOUND_Y + position.y, 0);

    }

    private void updatePosition(float deltaTime) {
        move(deltaTime);
        checkBounds();
    }

    @Override
    public void move(float deltaTime) {
        isLoggingMovement(false, deltaTime);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.lastAnimation = EntityAnimation.Anim.run;
            this.lastDirection = EntityAnimation.Direction.up
            ;
            position.y += GameWorld.WorldVars.PLAYER_SPEED_Y;


        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.lastAnimation = EntityAnimation.Anim.run;
            this.lastDirection = EntityAnimation.Direction.left;
            position.x -= GameWorld.WorldVars.PLAYER_SPEED_X;


        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.lastAnimation = EntityAnimation.Anim.run;
            this.lastDirection = EntityAnimation.Direction.down;
            position.y -= GameWorld.WorldVars.PLAYER_SPEED_Y;


        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.lastAnimation = EntityAnimation.Anim.run;
            this.lastDirection = EntityAnimation.Direction.right;
            position.x += GameWorld.WorldVars.PLAYER_SPEED_X;

        }
        else{
            this.lastAnimation = EntityAnimation.Anim.idle;
        }

    }

    private void checkBounds() {
        if (this.position.x + this.BOUND_X + speed.x < position.x && this.lastDirection.equals(EntityAnimation.Direction.left)) {
            speed.x = 0f;
        }
    }

    @Override
    public void isLoggingMovement(boolean isActive, float deltaTime) {
        if (isActive) {
            this.logTimer += deltaTime;
            if (this.logTimer > 15f) {
                Gdx.app.log("Player", toString());
                this.logTimer = 0f;
            }
        }

    }


    @Override
    public String toString() {
        return "\n Player_{ \n" +
                "\t speed= " + speed + "\n" +
                "\t position= " + position + " \n" +
                "\t Sprite Width  =  " + getSprite().getWidth() + " " +
                "\t Sprite Height = " + getSprite().getHeight() + " \n" +
                "\t Sprite Rectangle X = " + getSprite().getBoundingRectangle().x + " \t" +
                "\t Sprite Rectangle X / 2=" + getSprite().getBoundingRectangle().x / 2f + " \n" +
                "\t Sprite Rectangle Y = " + getSprite().getBoundingRectangle().y + " \t" +
                "\t Sprite Rectangle Y / 2 =" + getSprite().getBoundingRectangle().y / 2f + " \n" +
                "\t gameObject=" + gameObject +
                "\t \n}";
    }


}

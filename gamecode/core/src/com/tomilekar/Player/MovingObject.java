package com.tomilekar.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.tomilekar.AnimationC.AnimationSpriteSorter;
import com.tomilekar.MainScreen;
import com.tomilekar.world.GameWorld;

public abstract class MovingObject extends EntityBase {


    public boolean isRight = true;
    public boolean isUp = false;
    float logTimer = 0f;
    private AnimationSpriteSorter.Direction lastDirection = AnimationSpriteSorter.Direction.right;
    private Sprite sprite;
    public Vector2 speed;


    public MovingObject(MainScreen mainScreen, GameWorld world, Texture texuture,GameObject gameObject) {
        super(mainScreen, world, texuture, gameObject, true);
        this.sprite = new Sprite(texuture);
        this.speed = new Vector2(GameWorld.WorldVars.PLAYER_SPEED_X,GameWorld.WorldVars.PLAYER_SPEED_Y);
    }

    public abstract void move(float deltaTime);

    public abstract void isLoggingMovement(boolean active, float deltaTime);
    public abstract void startAnimation(Enum animation, Enum direction, float deltaTime);

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

}

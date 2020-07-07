package com.tomilekar;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.tomilekar.AnimationC.AnimationSpriteSorter;
import com.tomilekar.Player.*;
import com.tomilekar.Player.Animation;
import com.tomilekar.helpers.GameInfo;
import com.tomilekar.helpers.Tags;
import com.tomilekar.world.GameWorld;

import javax.xml.stream.FactoryConfigurationError;

public class MainScreen extends Game {


    public OrthographicCamera orthographicCamera;
    private GameWorld world;
    public SpriteBatch spriteBatch;


    float stateTime;



    Player_ player_2;
    Monster monster;
    EntityBase entityBase;

    EntityAnimation entityAnimation_monster;
    EntityAnimation entityAnimation_char;

    @Override
    public void create() {



        spriteBatch = new SpriteBatch();

        orthographicCamera = new OrthographicCamera(GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);

        world = new GameWorld(this);
        world.init();

        //   player = new Player(world.getWorld(), this);
        //  player.init(AnimationSpriteSorter.Directory.charakter);

        monster = new Monster(world.getWorld(), this);
        monster.init(AnimationSpriteSorter.Directory.monster);
        Texture test = new Texture(Gdx.files.internal("char_1.png"));
        player_2 = new Player_(this, world, test, "Player_2");
        player_2.initEntity();
        orthographicCamera.position.x = player_2.getPosition().x;
        orthographicCamera.position.y = player_2.position.y;
    }

    @Override
    public void render() {
        super.render();
        float deltaTime = Gdx.graphics.getDeltaTime();
        stateTime += Gdx.graphics.getDeltaTime();
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        orthographicCamera.update();


        spriteBatch.setProjectionMatrix(orthographicCamera.combined);

        world.render(deltaTime);
        spriteBatch.begin();

        monster.update(stateTime, spriteBatch);
        // spriteBatch.draw(player.getTextureRegion(), 300f, 400f);

        player_2.draw(stateTime);
        spriteBatch.end();
    }


    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public void followCamera()

    void move(){
        if(Gdx.input.isButtonPressed(Input.Keys.W)){
            player_2.isUp = true;
            player_2.isRight = false;

            player_2.position.y = GameWorld.WorldVars.PLAYER_SPEED_Y;


        } else if(Gdx.input.isButtonPressed(Input.Keys.A)){
            player_2.isRight = false;
            player_2.isRight = false;
            player_2.position.x -= GameWorld.WorldVars.PLAYER_SPEED_X;


        } else if(Gdx.input.isButtonPressed(Input.Keys.S)){
            player_2.isRight = false;
            player_2.isUp = false;
            player_2.position.y -= GameWorld.WorldVars.PLAYER_SPEED_Y;


        } else if(Gdx.input.isButtonPressed(Input.Keys.D)){
            player_2.isUp = true;
            player_2.isUp = false;
            player_2.speed.x += GameWorld.WorldVars.PLAYER_SPEED_X;

        }
        player_2.move(Gdx.graphics.getDeltaTime());
    }


}

package com.tomilekar.Player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tomilekar.AnimationC.AnimationSpriteSorter;
import com.tomilekar.AnimationC.AnimationSpriteSorter_2;
import org.w3c.dom.Text;

import java.util.HashMap;

public class Animation {


    private  HashMap<Enum, HashMap<Enum, com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>>> animationsMap;


    private HashMap<Enum, com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>> idle_anims;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> idle_right;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> idle_up;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> idle_left;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> idle_down;


    private HashMap<Enum, com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>> run_anims;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> run_right;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> run_up;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> run_left;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> run_down;



    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> witch_walk;
    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> witch_idle;

    private AnimationSpriteSorter.Directory directory;


    public Animation(AnimationSpriteSorter.Directory directory) {
        this.directory = directory;
    }

    public void init(){
   //     if(directory.equals(AnimationSpriteSorter.Directory.monster)){
     //       animationsMap = AnimationSpriteSorter.textureRegionsMap(AnimationSpriteSorter.Animation_.values(),
         //           AnimationSpriteSorter.Monsters.values(), directory);
       // }
    //    if(directory.equals(AnimationSpriteSorter.Directory.charakter)){
     //       animationsMap = AnimationSpriteSorter.textureRegionsMap(AnimationSpriteSorter.Direction.values(),
     //               AnimationSpriteSorter.Animation_.values(), directory);
     //   }


        if(directory.equals(AnimationSpriteSorter.Directory.charakter)){
            idle_anims = animationsMap.get(AnimationSpriteSorter.Animation_.idle);
            idle_down = idle_anims.get(AnimationSpriteSorter.Direction.down);
            idle_up = idle_anims.get(AnimationSpriteSorter.Direction.up);
            idle_right = idle_anims.get(AnimationSpriteSorter.Direction.right);
            idle_left = idle_anims.get(AnimationSpriteSorter.Direction.left);

            run_anims = animationsMap.get(AnimationSpriteSorter.Animation_.run);
            run_down = run_anims.get(AnimationSpriteSorter.Direction.down);
            run_up = run_anims.get(AnimationSpriteSorter.Direction.up);
            run_right = run_anims.get(AnimationSpriteSorter.Direction.right);
            run_left = run_anims.get(AnimationSpriteSorter.Direction.left);
        }else {
            idle_anims = animationsMap.get(AnimationSpriteSorter.Monsters.witch);
            witch_idle = idle_anims.get(AnimationSpriteSorter.Animation_.idle);
            witch_walk = idle_anims.get(AnimationSpriteSorter.Animation_.walk);
        }

    }


    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getIdle_right() {
        return idle_right;
    }

    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getIdle_up() {
        return idle_up;
    }

    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getIdle_left() {
        return idle_left;
    }

    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getIdle_down() {
        return idle_down;
    }

    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getRun_right() {
        return run_right;
    }


    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getRun_up() {
        return run_up;
    }


    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getRun_left() {
        return run_left;
    }


    public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getRun_down() {
        return run_down;
    }


}

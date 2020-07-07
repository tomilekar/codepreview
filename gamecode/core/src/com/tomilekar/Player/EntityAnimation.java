package com.tomilekar.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tomilekar.helpers.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class EntityAnimation {

    public static enum Direction {
        down, up, left, right
    }

    public static enum Anim {
        idle, run, walk

    }

    public static enum Entity {
        charakter, monster
    }

    public static enum Monster {
        witch
    }

    public static enum Player {
        basic
    }

    private Enum[] directions;
    private Enum[] animations;
    private Enum entity;
    private Enum[] entityTypes;


    // Animations with no direction
    private HashMap<Enum, Array<TextureRegion>> noDirectionTextureRegions;
    // Animations with directions, Animation, HashMap<Direction, Animation>
    private HashMap<Enum, HashMap<Enum, Array<TextureRegion>>> directionTextureRegions;

    private HashMap<Enum, Animation<TextureRegion>> noDirectionAnimationsMap;
    // Animations with directions, Animation, HashMap<Direction, Animation>
    private HashMap<Enum, HashMap<Enum, com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>>> directionAnimationsMap;

    // DIRECTION Animation

    private HashMap<Enum, Animation<TextureRegion>> directionAnimation;

    public EntityAnimation(Enum[] directions, Enum[] anims, Enum entity, Enum[] entityTypes) {
        this.animations = anims;
        this.entity = entity;
        this.entityTypes = entityTypes;
        this.directions = directions;
    }

    public void initAnimations() {
        startLoop();
        finishLog();

    }


    private void finishLog() {

        int animations = 0;
        int directions = 0;
        if (noDirectionAnimationsMap.size() > 0) {
            animations = noDirectionAnimationsMap.size();
            Gdx.app.log("Animations Loaded for " + this.entity.toString(), String.valueOf(animations));
        } else if (directionAnimationsMap.size() > 0) {
            directions = directionAnimationsMap.get(this.animations[0]).size();
            animations = directionAnimationsMap.size();
            Gdx.app.log(this.entity.toString()
                    , "Animations Loaded for " +"Animations "+ String.valueOf(animations) + "Directtions  " + String.valueOf(directions));
        }

    }


    private void startLoop() {
        if (entityTypes.length == 0 && entity == null) {
            System.out.println(" initDirectionAnims: " + "entity & types null");
            return;

        }
        if (animations.length == 0 && directions.length == 0) {
            System.out.println(" initDirectionAnims: " + " anim & dir null");
            return;

        }
        if (animations.length == 0) {
            System.out.println(" initDirectionAnims: " + " anim ");
            return;
        }


        this.noDirectionAnimationsMap = new HashMap<>();
        this.directionAnimationsMap = new HashMap<>();
        loopEntityTypes();

    }

    private void loopEntityTypes() {
        final Enum[] entityTypes = this.entityTypes;

        for (Enum currentEntity : entityTypes) {
            final StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(this.entity);
            pathBuilder.append(StringUtils.getPathPart(currentEntity));
            Gdx.app.log("EntityAnimation: ", "currentEntity: " + currentEntity);
            Gdx.app.log("Message 1: currentEntity ", pathBuilder.toString());
            loopAnimations(pathBuilder.toString());

        }

    }

    private void loopAnimations(String path) {
        final Enum[] animations = this.animations;
        for (Enum currentAnimation : animations) {
            final StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(path).append(StringUtils.getPathPartNoSuffixSlash(currentAnimation));
            final FileHandle animationCheck = Gdx.files.internal(pathBuilder.toString());
            if (!checkFileHandleISexisting(pathBuilder.toString(), currentAnimation)) {
                continue;
            }
            Gdx.app.log("EntityAnimation: ", "currentAnimation: " + currentAnimation);
            Gdx.app.log("Message 2: currentAnimation ", pathBuilder.toString());
            if (this.directions == null) {
                Gdx.app.log("Message 3 :", " NO DIRECTIONS");
                createTextureRegions(pathBuilder.toString(), currentAnimation, null,null);
                continue;
            }

            loopDirections(pathBuilder.toString(), currentAnimation);

        }
    }

    private void loopDirections(String path, Enum animation) {
        final Enum anim = animation;
        final Enum[] directions = this.directions;
        final HashMap<Enum, Animation<TextureRegion>> directionMap = new HashMap<>();
        for (Enum currentDirection : directions) {
            final StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(path).append(StringUtils.getPathPartNoSuffixSlash(currentDirection));
            if (!checkFileHandleISexisting(pathBuilder.toString(), currentDirection)) {
                continue;
            }
            Gdx.app.log("Message 3: ", "found directions :" + pathBuilder.toString());

            createTextureRegions(pathBuilder.toString(), anim, currentDirection, directionMap);

            this.directionAnimationsMap.put(anim,directionMap);
        }

    }

    private void createTextureRegions(String pathAtlas, Enum animation, Enum direction, HashMap<Enum, Animation<TextureRegion>> dirMap) {

        if (animation == null) {
            Gdx.app.log("animation cant be null:", " createTextureRegion");
        }


        final StringBuilder pathBuilder = new StringBuilder();

        final Array<TextureRegion> textureRegionsArray = new Array<>();

        if (direction == null) {


            pathBuilder.append(pathAtlas).append(StringUtils.getAtlas(animation, null));
            final TextureAtlas textureAtlas = getTextureRegionOfAtlast(pathBuilder.toString());
            fillTextureRegionArray(textureRegionsArray, textureAtlas, animation, direction);
            final Animation<TextureRegion> createdAnimation = new Animation<TextureRegion>(1f / textureRegionsArray.size, textureRegionsArray);
            createdAnimation.setPlayMode(Animation.PlayMode.LOOP);
            if (createdAnimation != null) {
                this.noDirectionAnimationsMap.put(animation, createdAnimation);
            }


        }

        if (direction != null && dirMap != null) {
            pathBuilder.append(pathAtlas).append(StringUtils.getAtlas(animation, direction));
            final TextureAtlas textureAtlas = getTextureRegionOfAtlast(pathBuilder.toString());
            fillTextureRegionArray(textureRegionsArray, textureAtlas, animation, direction);
            final String animation_direction = StringUtils.getRegion(animation, direction);
            final Animation<TextureRegion> createdAnimation = new Animation<TextureRegion>(1f / textureRegionsArray.size, textureRegionsArray);
            createdAnimation.setPlayMode(Animation.PlayMode.LOOP);
            if (createdAnimation != null) {
                dirMap.put(direction, createdAnimation);
            }
        }


    }


    private boolean checkFileHandleISexisting(String path, Enum checkEnum) {
        final FileHandle checkFileHandle = Gdx.files.internal(path);
        if (!checkFileHandle.exists()) {
            Gdx.app.log("Error: ", "didnt find " + checkEnum);
            return false;
        }
        return true;
    }

    private void fillTextureRegionArray(Array<TextureRegion> textureRegions, TextureAtlas textureAtlas, Enum animation, Enum direction) {
        if (animation == null && direction == null) {
            Gdx.app.log("Error filling TextureRegions, +", animation.toString());
            return;
        }


        //  final String region = StringUtils.getRegion(animation,direction);
        final String region = StringUtils.getRegion(animation, direction);

        int textureAtlasSize = textureAtlas.getRegions().size;
        int counter = 0;
        for (int i = 0; i < textureAtlasSize; i++) {

            final StringBuilder regionBuilder = new StringBuilder();
            regionBuilder.append(region).append("_").append(counter);
            final TextureRegion currentRegion =  textureAtlas.findRegion(regionBuilder.toString());
            if (currentRegion == null) {
                Gdx.app.log("Cant find Region", regionBuilder.toString());
                continue;
            }

            textureRegions.add(currentRegion);
            counter++;
        }


    }


    private TextureAtlas getTextureRegionOfAtlast(String internal) {
        final FileHandle fileHandle = Gdx.files.internal(internal);
        if (fileHandle == null) {
            Gdx.app.log("Atlas dosnt exist: ", internal);
            return null;
        }

        return new TextureAtlas(fileHandle);
    }

    public HashMap<Enum, Animation<TextureRegion>> getNoDirectionAnimationsMap() {
        return this.noDirectionAnimationsMap;
    }

    public HashMap<Enum, HashMap<Enum, Animation<TextureRegion>>> getDirectionAnimationsMap() {
        return this.directionAnimationsMap;
    }

    public void debugDirectionAnimation(){
        if(this.directionAnimationsMap.size() > 0){
            for(Map.Entry<Enum,HashMap<Enum,Animation<TextureRegion>>> curEntry : this.directionAnimationsMap.entrySet()){
                System.out.println(curEntry.getKey().toString() + " contains\n =========================== ");
                for(Map.Entry<Enum, Animation<TextureRegion>> curAnimEntry : curEntry.getValue().entrySet()){

                    Animation<TextureRegion>  test = curAnimEntry.getValue();
                    test.getAnimationDuration();
                    System.out.println(curAnimEntry.getKey() + " Duration : " + test.getAnimationDuration() );
                }
                System.out.println(" \t");
            }

        }
    }
    public void debugNODirectionAnimation(){
        if(this.noDirectionAnimationsMap.size() > 0){

        }
    }

}

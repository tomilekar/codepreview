package com.tomilekar.desktop.tools.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.tomilekar.helpers.GameInfo;
import com.tomilekar.helpers.StringUtils;
import com.tomilekar.world.GameWorld;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Arrays;

public class AnimHelper {
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

    public static void main(String[] args) {


        for (Entity currentEntity : Entity.values()) {
            final StringBuilder animationFolder = new StringBuilder();
            animationFolder.append(GameInfo.System_Path).append(GameInfo.ANIMATIONS_FOLDER);

            final StringBuilder assetsFolder = new StringBuilder();
            assetsFolder.append(GameInfo.System_Path).append(currentEntity).append("/");

            final StringBuilder entityFolder = new StringBuilder();
            entityFolder.append(animationFolder).append(currentEntity).append("/");


            System.out.println(" Current working Directory: " + entityFolder.toString());
            if (currentEntity.equals(Entity.monster)) {
                startLoop(Monster.values(), Anim.values(), null, entityFolder.toString(),assetsFolder.toString());
            } else if (currentEntity.equals(Entity.charakter)) {
                startLoop(Player.values(), Anim.values(), Direction.values(), entityFolder.toString(),assetsFolder.toString());
            }
        }
    }

    // Entitytype , animations, direction
    public static void startLoop(Enum[] entityType, Enum[] anims, Enum[] direction, String entityDir,String outputAssets) {
        final String entityFolder = entityDir;
        final Enum[] entityT = entityType;
        final Enum[] animations = anims;
        final Enum[] directions = direction;
        loopEntityType(entityT, animations, entityFolder, directions,  outputAssets );
    }

    public static void loopEntityType(Enum[] entityType, Enum[] animations, String workingDir, Enum[] directions, String outputAssets) {
        final Enum[] anim = animations;

        for (Enum currentEntity : entityType) {
            final StringBuilder entityTypeFolder = new StringBuilder();
            entityTypeFolder.append(workingDir).append(currentEntity).append("/");

            final StringBuilder outputAsset = new StringBuilder();
            outputAsset.append(outputAssets).append(currentEntity).append("/");
            final File entityTypeFile = new File(entityTypeFolder.toString());
            System.out.println(" Current EntityTypesFolder:    " + entityTypeFolder.toString());

            if (entityTypeFile.exists()) {
                loopAnimations(anim, entityTypeFolder.toString(), directions,outputAsset.toString(),currentEntity);

            }
        }
    }

    public static void loopAnimations(Enum[] animations, String entityTypeDir, Enum[] directions,String outputAsset, Enum entityType) {
        for (Enum currentAnimation : animations) {
            final StringBuilder animationFolder = new StringBuilder();
            animationFolder.append(entityTypeDir).append(currentAnimation).append("/");
            final File checkFile = new File(animationFolder.toString());

            if(!checkFile.exists()){
                continue;
            }
            final StringBuilder outputAssets = new StringBuilder();
            outputAssets.append(outputAsset).append(currentAnimation).append("/");
            System.out.println(" Current AnimationFolder:   " + animationFolder.toString());
            if (directions == null) {
                outputAnimations(animationFolder.toString(),currentAnimation,null, outputAssets.toString(), entityType);
                continue;
            }
            loopDirections(directions, currentAnimation,animationFolder.toString(),outputAssets.toString(),  entityType);

        }
    }

    public static void loopDirections(Enum[] directions,Enum animation, String animationFolder,String outputAsset, Enum entityType) {
        for (Enum currentDirection : directions) {
            final StringBuilder currentDirectionFolder = new StringBuilder();
            currentDirectionFolder.append(animationFolder).append(currentDirection).append("/");
            System.out.println(" Current DirectionFolder:   " + currentDirectionFolder.toString());

            final StringBuilder outputAssets = new StringBuilder();
            outputAssets.append(outputAsset).append(currentDirection).append("/");
            outputAnimations(currentDirectionFolder.toString(),animation,currentDirection,outputAssets.toString(), entityType);
        }
    }

    public static void outputAnimations(String inputFolder,Enum animation, Enum direction, String outputAsset, Enum entityType){

        final StringBuilder outputAtlas = new StringBuilder();
        outputAtlas.append(animation);

        final StringBuilder outputAssets = new StringBuilder();
        outputAssets.append(outputAsset);
        System.out.println(" Current outputAsset: " + outputAssets.toString());
        System.out.println(outputAssets.toString());
        if(direction != null){
            outputAtlas.append("_").append(direction);
        }
        System.out.println("Atlas outputname: " + outputAtlas.toString());
        final StringBuilder outputFolder = new StringBuilder();
        outputFolder.append(inputFolder).append("output");
        System.out.println(" Current Inputfolder: " + inputFolder);
        System.out.println(" Current Outputfolder:   " + outputFolder.toString());
        final File outputAtlasFolder = new File(outputFolder.toString());
        if(!outputAtlasFolder.exists()){
            outputAtlasFolder.mkdir();
        }


        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.useIndexes = false;
        TexturePacker.process(settings,inputFolder,outputFolder.toString(), outputAtlas.toString());

        File[] files = new File(inputFolder.toString()).listFiles();
        Arrays.sort(files, 0, files.length-1);

        int counter = 0;
        try{
            for(File file2 : files) {
                if (file2.getName().contains("DS_Store")) {
                    continue;
                }




                final StringBuilder newFileName = new StringBuilder();
                newFileName.append(StringUtils.getRegion(animation,direction)).append("_");
                newFileName.append(counter).append(".png");

                final StringBuilder animationsPath = new StringBuilder();
                animationsPath.append(file2.getParent()).append("/");
                final StringBuilder assetPath = new StringBuilder();
                assetPath.append(outputAsset);


                final StringBuilder assetFile = new StringBuilder();
                assetFile.append(assetPath).append(newFileName);
                final StringBuilder animationsFile = new StringBuilder();
                animationsFile.append(animationsPath).append(newFileName);
                final File renameFile = new File(animationsFile.toString());
                final File copyFile = new File(assetFile.toString());


                if(file2.isDirectory()){
                    for(File atlasFile : file2.listFiles()){

                        final StringBuilder atlasFileTransfer = new StringBuilder();
                        atlasFileTransfer.append(assetPath).append(atlasFile.getName());

                        File renameedFile = new File(atlasFileTransfer.toString());

                        atlasFile.renameTo(renameedFile);
                        Files.copy(renameedFile.toPath(), new BufferedOutputStream(new FileOutputStream(atlasFile)));
                    }
                    continue;

                }
             file2.renameTo(renameFile);

              Files.copy(renameFile.toPath(), new BufferedOutputStream(new FileOutputStream(copyFile)));

               // file2.renameTo(createdFile);
                //file2.renameTo(createdFile);
                counter++;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());

        }



        //TexturePacker.process(inputFolder,inputFolder,outputAtlas.toString());



       //File[] files = outputAtlasFolder.listFiles();
      // copyFilesToWorkingDirectory(files,outputAssets.toString());



    }

    public static void copyFilesToWorkingDirectory(File[] files, String workingDirectory)  {
       try{
           for(File currentLoopFile : files){
               if(currentLoopFile.getName().startsWith(".")){
                   continue;
               }
               System.out.println(" Current Edited File    " + currentLoopFile.getName());
               final File createdFile = new File(workingDirectory + currentLoopFile.getName());
               final File workingDir = new File(workingDirectory);
               clearTargetDirectory(workingDir);
               Files.move(currentLoopFile.toPath(),createdFile.toPath() );
           }
       }catch (Exception e){
           e.printStackTrace();
       }


    }

    public static void clearTargetDirectory(File fileInput){
        File[] files = fileInput.listFiles();
        for(File file : files){
            if(file.exists() && !file.getName().startsWith(".")){
                file.delete();
            }
        }

    }

}

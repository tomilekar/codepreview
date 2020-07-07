

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;


public class AnimationSpriteSorter {

    public static enum Direction {
        down, up, left, right
    }

    public static enum Animation_ {
        idle, run, walk

    }

    public static enum Directory{
        charakter,monster
    }
    public static enum Monsters {
        witch
    }


    public final static String GAMEPATH_SYS = "/Users/tomilekar/Desktop/IsometricGame/";
    public final static String PREFIX = "/core/assets/Charakter/animations/";
    public final static float FRAME_DURATION = 0.2f;
    public final static String Helper = "AnimationSpriteHelper:";




    public static  Animation<TextureRegion> getAnimation(Enum direction,  HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction, Animation<TextureRegion>> animDir){
        return animDir.get(direction);
    }
    public static HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction, Animation<TextureRegion>> getDirectionAnimationMap(Enum animation_, HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_, HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction, Animation<TextureRegion>>> complex){
        return complex.get(animation_);
    }
    public static HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_, HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction, Animation<TextureRegion>>> textureRegionsMap(com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction[] directions, com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_[] animations) {


        // List of animations :
        // Run ->  Direction , Animation
        // Idle ->  Direction, Animation
        final String path = "./Charakter/";
        HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_, HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction, Animation<TextureRegion>>> animationsMap = new HashMap<>();

        long startTime = System.nanoTime();
        Gdx.app.log(Helper, " start time : " + startTime / 1000000);
        for (com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_ animation : animations) {
            HashMap<com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction, Animation<TextureRegion>> direction_animations = new HashMap<>();
            for (com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction direction : directions) {
                Animation<TextureRegion> anim = null;
                final Array<TextureRegion> textureRegions = new Array<>();
                final StringBuilder anim_direction = new StringBuilder();
                anim_direction.append(animation).append("_").append(direction);

                final StringBuilder textureAtlasPath = new StringBuilder();
                textureAtlasPath.append(path).append(animation).append("/");
                textureAtlasPath.append(anim_direction).append(".txt");
                TextureAtlas textureAtlas = new TextureAtlas
                        (Gdx.files.internal(textureAtlasPath.toString()));
                if (textureAtlas == null) {
                    continue;
                }
                Gdx.app.log("AnimationSpriteHelper:", " found : " + textureAtlasPath.toString());

                int iterations = textureAtlas.getRegions().size;
                int counter = 0;
                for (int i = 0; i < iterations; i++) {
                    final StringBuilder region = new StringBuilder();
                    region.append(anim_direction).append("_").append(counter);
                    final TextureRegion animationRegion = textureAtlas.findRegion(region.toString());
                    textureRegions.add(animationRegion);
                    counter++;

                }
                anim = new Animation<TextureRegion>(FRAME_DURATION, textureRegions);
                anim.setPlayMode(Animation.PlayMode.LOOP);

                direction_animations.put(direction, anim);
                animationsMap.put(animation, direction_animations);

            }
        }
        long endtime = System.nanoTime();
        long elapsed = endtime - startTime;
        Gdx.app.log(Helper, " End time : " + elapsed / 1000000);

        return animationsMap;
    }

    public static void main(String[] args) {


        String prefix = GAMEPATH_SYS + PREFIX;
        final com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_[] animations = com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_.values();
        final com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction[] directions = com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction.values();
        int animationCounter = 0 ;
        for (final com.tomilekar.AnimationC.AnimationSpriteSorter_2.Animation_ animation : animations) {
            final StringBuilder folder = new StringBuilder();
            folder.append(prefix);
            folder.append(animation.toString());


            for (com.tomilekar.AnimationC.AnimationSpriteSorter_2.Direction direction : directions) {
                final StringBuilder currentDirection = new StringBuilder();
                currentDirection.append(folder).append("/");
                currentDirection.append(direction);


                final StringBuilder newFilename = new StringBuilder();
                newFilename.append(currentDirection);
                newFilename.append("/");
                newFilename.append(animation).append("_").append(direction).append("_").append("");

                System.out.println(newFilename.toString());
                System.out.println(currentDirection.toString());
                final File file = new File(currentDirection.toString());
                if (file == null) {
                    continue;
                }

                File[] files = file.listFiles();

                Comparator<File> comparator = new Comparator<File>() {
                    @Override
                    public int compare(File file, File t1) {

                        if(!file.getName().startsWith(animation.toString())|| !t1.getName().startsWith(animation.toString())){
                            return 0;
                        }
                        int int_1 = getNumbersOfFile(file);
                        int int_2 = getNumbersOfFile(t1);
                        if(int_1 > int_2){
                            return 1;
                        }else if( int_1 == int_2){
                            return 0;
                        }else if(int_1< int_2){
                            return -1;
                        }
                        return 0;
                    }

                };
                Arrays.sort(files, comparator);



                assert files != null;
                int counter = 0;
                for(File file1 : files) {
                    if(!file1.getName().startsWith(animation.toString())) {
                        continue;
                    }

                    final StringBuilder finalFileName = new StringBuilder();
                    finalFileName.append(newFilename);
                    finalFileName.append(counter).append(".png");
                    System.out.println(finalFileName.toString());
                    final File createFile = new File(finalFileName.toString());
                    file1.renameTo(createFile);
                    counter++;

                }
                //   for(int i = 0; i <files.length; i++){
                //      final StringBuilder finalFilename = new StringBuilder();
                ///     finalFilename.append(newFilename);
                //    finalFilename.append(i).append(".png");
                //  File currFile = files[i];
                // if(currFile.getName().startsWith(animation)){
                //   continue;
                // }



            }


        }

    }



    public static int getNumbersOfFile(File file){

        String string = file.getName();
        string =  string.replaceAll("\\D","");
        return Integer.parseInt(string);

    }

    static void log(String msg) {
        System.out.println(Helper + msg);
    }
}


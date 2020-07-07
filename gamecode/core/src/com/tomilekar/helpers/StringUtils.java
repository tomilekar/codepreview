package com.tomilekar.helpers;

import com.badlogic.gdx.Gdx;

public class StringUtils {


    public final static String textAtlasSuffix = ".atlas";
    public static String getPathPart(Enum part){
        if(part == null){
            Gdx.app.log("ERROR :", "getPathpart() EntityAnimation");
            return "";
        }
        final StringBuilder pathPart = new StringBuilder();
        pathPart.append("/").append(part).append("/");
        return pathPart.toString();
    }
    public static String getPathPartNoSuffixSlash(Enum part){
        if(part == null){
            Gdx.app.log("ERROR :", "getPathpart() EntityAnimation");
            return "";
        }
        final StringBuilder pathPart = new StringBuilder();
        pathPart.append(part).append("/");
        return pathPart.toString();
    }

    // returns animation_direction.atlas
    public static String getAtlas(Enum animation, Enum direction){
        if(animation == null && direction == null){
            Gdx.app.log("Error Util", "getAtlastDirection");
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        if(direction == null){
            stringBuilder.append(animation).append("").append(textAtlasSuffix);
            return stringBuilder.toString();
        }
        stringBuilder.append(animation).append("_").append(direction).append(textAtlasSuffix);

        return stringBuilder.toString();
    }

    public static String getRegion(Enum animation, Enum direction){
        if(animation == null && direction == null){
            Gdx.app.log("Error Util", "getAtlastDirection");
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        if(direction == null){
            stringBuilder.append(animation);
            return stringBuilder.toString();
        }
        stringBuilder.append(animation).append("_").append(direction);

        return stringBuilder.toString();
    }
}

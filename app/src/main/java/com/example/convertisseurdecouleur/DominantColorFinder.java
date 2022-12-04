package com.example.convertisseurdecouleur;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import java.util.HashMap;

public class DominantColorFinder {

    Bitmap bitmap;
    HashMap<Color, Integer> colorMap;
    HashMap<Color, Float> weightMap;

    HashMap<Color, Float> groupColorMap;

    HashMap<Integer, Color> dominantColors;

    public DominantColorFinder(Bitmap _bitmap) {
        bitmap = _bitmap;
        colorMap = new HashMap<>();
        weightMap = new HashMap<>();
        groupColorMap = new HashMap<>();
        dominantColors = new HashMap<>();
    }

    public HashMap<Integer, Color> getDominantColors() {
        preProcessImage();
        Color foundColor = new Color();
        buildWeightMap(1); // neutral
        foundColor = degradeColor(foundColor, 6);
        //foundColor = degradeColor(foundColor, 4);
        //foundColor = degradeColor(foundColor, 2);
        //foundColor = degradeColor(foundColor, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            foundColor = Color.valueOf(foundColor.red()/255, foundColor.green()/255, foundColor.blue()/255);
        }
        dominantColors.put(1, foundColor);
        System.out.println("COLOR: " +  foundColor);

        /*groupColorMap = new HashMap<>();
        buildWeightMap(1); // hue
        degradeColor(6);
        degradeColor(4);
        degradeColor(2);
        degradeColor(0);
        dominantColors.put(2, (Color) groupColorMap.keySet().toArray()[0]);
        System.out.println("COLOR: " +  groupColorMap.keySet().toArray()[0]);

        groupColorMap = new HashMap<>();
        buildWeightMap(3); // bright
        degradeColor(6);
        degradeColor(4);
        degradeColor(2);
        degradeColor(0);
        dominantColors.put(3, (Color) groupColorMap.keySet().toArray()[0]);
        System.out.println("COLOR: " +  groupColorMap.keySet().toArray()[0]); */

        return dominantColors;
    }

    protected void preProcessImage() {
        // Construction de la Hashmap
        for(int x = 0; x < bitmap.getWidth(); x++) {
            for(int y = 0; y < bitmap.getHeight(); y++) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    Color key = bitmap.getColor(x, y);
                    if(colorMap.containsKey(key)) {
                        colorMap.put(key, colorMap.get(key)+1);
                    } else {
                        colorMap.put(key, 1);
                    }
                }
            }
        }
    }

    protected void buildWeightMap(int mode) {
        switch(mode) {
            case 1: // FAVOR HUE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    float r, g, b;
                    for(Color key : colorMap.keySet()) {
                        r = key.red();
                        g = key.green();
                        b = key.blue();
                        weightMap.put(key, (Math.abs(r-g)*Math.abs(r-g) + Math.abs(r-b)*Math.abs(r-b) + Math.abs(g-b)*Math.abs(g-b))/65535*50+1);
                    }
                }
                break;
            case 2: // FAVOR BRIGHT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    for(Color key : colorMap.keySet()) {
                            weightMap.put(key, 1f + key.red() + key.green() + key.blue());
                    }
                }
                break;
            default: // DEFAULT BEHAVIOR
                for(Color key : colorMap.keySet())
                    weightMap.put(key, 1f);
                break;
        }

    }

    protected Color degradeColor(Color comparativeColor, int remainingBits) {

        groupColorMap.put(comparativeColor, 0f);
        for(Color key : colorMap.keySet()) {
            float trueWeight = colorMap.get(key) * weightMap.get(key);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int r = (int) Math.floor(key.red() >= 1.0 ? 255 : key.red() * 256.0);
                int g = (int) Math.floor(key.green() >= 1.0 ? 255 : key.green() * 256.0);
                int b = (int) Math.floor(key.blue() >= 1.0 ? 255 : key.blue() * 256.0);
                Color groupKey = Color.valueOf((r>>remainingBits)/255, (g>>remainingBits)/255, (b>>remainingBits/255));

                // Update group maps
                if(groupColorMap.containsKey(groupKey)) {
                    groupColorMap.put(groupKey, groupColorMap.get(groupKey)+trueWeight);
                } else {
                    groupColorMap.put(groupKey, trueWeight);
                }
            }
        }

        // Return the most prominent color in the degraded map
        Color returnedColor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            returnedColor = Color.valueOf(0);
        }
        for(Color key : groupColorMap.keySet()) {
            if(groupColorMap.get(key) >= groupColorMap.get(comparativeColor)) {
                returnedColor = key;
                comparativeColor = key;
            }
        }
        return returnedColor;
    }

}

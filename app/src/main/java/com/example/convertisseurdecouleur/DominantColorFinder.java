package com.example.convertisseurdecouleur;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DominantColorFinder {

    Bitmap bitmap;
    HashMap<Integer, Integer> colorMap;
    HashMap<Integer, Float> weightMap;

    List<String> dominantColors;

    public DominantColorFinder(Bitmap _bitmap) {
        bitmap = _bitmap;
        colorMap = new HashMap<>();
        weightMap = new HashMap<>();
        //groupColorMap = new HashMap<>();
        dominantColors = new ArrayList<>();
    }

    public List<String> getDominantColors() {
        preProcessImage();
        buildWeightMap(0); // neutral
        int foundColor = -1;
        foundColor = degradeColorMap(foundColor, 6);
        int[] rgb = getRGBArr(foundColor);
        String finalColor = "#"
                + ("00" + Integer.toHexString(rgb[0])).substring(2)
                + ("00" + Integer.toHexString(rgb[1])).substring(2)
                + ("00" + Integer.toHexString(rgb[2])).substring(2);
        dominantColors.add(finalColor);
        System.out.println("COLOR: " +  finalColor + " FROMZ " + foundColor);

        return dominantColors;
    }

    // Construit la Hashmap avec des clés de la forme ColorInt
    protected void preProcessImage() {
        for(int x = 0; x < bitmap.getWidth(); x++) {
            for(int y = 0; y < bitmap.getHeight(); y++) {
                int key = bitmap.getPixel(x, y);
                if(colorMap.containsKey(key)) {
                    colorMap.put(key, colorMap.get(key)+1);
                } else {
                    colorMap.put(key, 1);
                }
            }
        }
    }

    protected void buildWeightMap(int mode) {
        switch(mode) {
            case 1: // FAVOR HUE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int[] rgb;
                    float r, g, b;
                    for(int key : colorMap.keySet()) {
                        rgb = getRGBArr(key);
                        r = rgb[0];
                        g = rgb[1];
                        b = rgb[2];
                        weightMap.put(key, (Math.abs(r-g)*Math.abs(r-g) + Math.abs(r-b)*Math.abs(r-b) + Math.abs(g-b)*Math.abs(g-b))/65535*50+1);
                    }
                }
                break;
            case 2: // FAVOR BRIGHT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    for(int key : colorMap.keySet()) {
                        int[] rgb = getRGBArr(key);
                            weightMap.put(key, 1f + rgb[0] + rgb[1] + rgb[2]);
                    }
                }
                break;
            default: // DEFAULT BEHAVIOR
                for(int key : colorMap.keySet())
                    weightMap.put(key, 1f);
                break;
        }

    }

    // Retourne la couleur la plus dominante dans la carte dégradée
    protected int degradeColorMap(int comparativeColor, int degradation) {

        HashMap<Integer, Float> groupColorMap = new HashMap<>();
        groupColorMap.put(comparativeColor, 0f);

        int colors = 0;
        for(int key : colorMap.keySet()) {
            float trueWeight = colorMap.get(key) * weightMap.get(key);
            colors++;
            if (colorsMatchAfterDegradation(key, comparativeColor, degradation)) {
                int groupKey = getDegradedColor(key, degradation);

                if (groupColorMap.containsKey(groupKey)) {
                    groupColorMap.put(groupKey, groupColorMap.get(groupKey) + trueWeight);
                } else {
                    groupColorMap.put(groupKey, trueWeight);
                }
            }
        }

        // Plus présente couleur dans la carte dégradée
        int returnedColor = -1;
        for(int key : groupColorMap.keySet()) {
            if(groupColorMap.get(key) >= groupColorMap.get(comparativeColor)) {
                returnedColor = key;
                comparativeColor = key;
            }
        }
        return returnedColor;
    }

    // Récupère les valeurs RGB (d'après la documentation de Color)
    protected int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return new int[]{red, green, blue, alpha};
    }

    // Regarde si deux couleurs sont "plus ou moins identiques"
    protected boolean colorsMatchAfterDegradation(int color1, int color2, int degradation) {
        if(color1 == -1) return true;
        int[] rgb1 = getRGBArr(color1);
        int[] rgb2 = getRGBArr(color2);
        for(int i = 0; i < 3; i++) {
            rgb1[i] = rgb1[i] >> degradation;
        }
        return rgb1[0] == rgb2[0] && rgb1[1] == rgb2[1] && rgb1[2] == rgb2[2];
    }

    // Renvoie une version dégradée d'une couleur (appartenance à un groupe de pixels)
    protected int getDegradedColor(int color, int degradation) {
        int[] rgb = getRGBArr(color);
        for(int i = 0; i < 3; i++) {
            rgb[i] = rgb[i] >> degradation;
        }
        return (rgb[3] & 0xff) << 24 | (rgb[0] & 0xff) << 16 | (rgb[1] & 0xff) << 8 | (rgb[2] & 0xff);

    }

}

package com.example.convertisseurdecouleur;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jittagornp
 * Adapted and improved by L. Herau
 *
 * thank you
 * http://stackoverflow.com/questions/10530426/how-can-i-find-dominant-color-of-an-image
 */
public class DominantColorFinderOpti {

    public static List<String> getHexColors(Bitmap bitmap) {

        Map<Integer, Integer> colorMap = new HashMap<>();

        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int rgb = bitmap.getPixel(i, j);
                if (!isGray(getRGBArr(rgb))) {
                    Integer counter = colorMap.get(rgb);
                    if (counter == null) {
                        counter = 0;
                    }

                    colorMap.put(rgb, ++counter);
                }
            }
        }

        List<String> hexColors = getMostCommonColors(colorMap, 4);

        return hexColors;
    }

    private static List<String> getMostCommonColors(Map<Integer, Integer> map, int n) {
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, (Map.Entry<Integer, Integer> obj1, Map.Entry<Integer, Integer> obj2)
                -> ((Comparable) obj1.getValue()).compareTo(obj2.getValue()));

        List<String> hexColors = new ArrayList<>();
        int x = 0; // compteurs de couleurs trouvées
        int i = 0;
        int color = 0, savedColor = 0;
        while(x < n && i < list.size()) { // Tant qu'on a pas trouvé toutes nos couleurs
            color = (list.get(list.size() - 1 - i).getKey());
            if(savedColor != color) { // TODO Remplacer ça par un algorithme de distance
                int[] rgb = getRGBArr(color);
                hexColors.add(intToHex(rgb));
                savedColor = color;
                x++;
            }
            i++;
        }

        return hexColors;
    }

    private static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return new int[]{red, green, blue};
    }

    private static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance) {
            if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false;
            }
        }
        return true;
    }

    private static String intToHex(int[] rgbArr) {
        String ret = "#";
        for(int i = 0; i < 3; i++) {
            String val = Integer.toHexString(rgbArr[i]);
            while(val.length() < 2) {
                val = "0" + val;
            }
            ret += val;
        }
        return ret;
    }
}

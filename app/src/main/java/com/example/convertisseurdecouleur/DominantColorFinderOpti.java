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
        for(int i = 0; i < n; i++) {
            int[] rgb;
            if(list.size() >= n) {
                rgb = getRGBArr((list.get(list.size() - n)).getKey());
            } else {
                rgb = getRGBArr((list.get(list.size() - 1)).getKey());
            }
            hexColors.add("#" + Integer.toHexString(rgb[0])
                    + Integer.toHexString(rgb[1])
                    + Integer.toHexString(rgb[2]));
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
}

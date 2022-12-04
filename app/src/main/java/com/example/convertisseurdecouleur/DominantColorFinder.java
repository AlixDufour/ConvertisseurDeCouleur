package com.example.convertisseurdecouleur;

import android.graphics.Bitmap;
import java.util.HashMap;

public class DominantColorFinder {

    static Bitmap bitmap;
    static HashMap<String, Integer> colorMap;

    public static void preProcessImage(Bitmap _bitmap) {
        bitmap = _bitmap;
        colorMap = new HashMap<>();

        // Construction de la Hashmap
        for(int x = 0; x < bitmap.getWidth(); x++) {
            for(int y = 0; y < bitmap.getHeight(); y++) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    String key = bitmap.getColor(x, y).toString();
                    if(colorMap.containsKey(key)) {
                        colorMap.put(key, colorMap.get(key)+1);
                    } else {
                        colorMap.put(key, 0);
                    }
                }
            }
        }
    }

    public static void getMostNeutralColor() {
        System.out.println();
    }

}

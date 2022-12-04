package com.example.convertisseurdecouleur;

import android.graphics.Color;

import java.util.regex.Pattern;

//doit pouvoir convertir entre rgb hsl hexa
public class Couleur {
    private Integer color;

    public Couleur(int red, int green, int blue){
        setFromRGB(red, green, blue);
    }

    public Couleur(int alpha, int red, int green, int blue){
        setFromARGB(alpha, red, green, blue);
    }

    public Couleur(int color){
        this.color=color;
    }

    public Couleur(String hex){
        if (hex.length()==7){
            setFromStringHEX(hex);
        }
        else if (hex.length()==9){
            setFromStringHEXalpha(hex);
        }
        else
            color = Color.BLACK;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int couleur){
        color = couleur;
    }

    public int setFromRGB(int red, int green, int blue){
        color= android.graphics.Color.rgb(red, green, blue);
        return color;
    }

    public int setFromARGB(int alpha, int red, int green, int blue){
        color= android.graphics.Color.argb(alpha, red, green, blue);
        return color;
    }

    public int setFromStringHEX(String hexa){
        String reg = "#[a-f0-9A-F]{6}";
        if (!Pattern.matches(reg, hexa)) {
            hexa = "#ffffff";
        }
        color= Color.parseColor(hexa);
        return color;
    }

    public int setFromStringHEXalpha(String hexa){
        String reg = "#[a-f0-9A-F]{8}";
        if (!Pattern.matches(reg, hexa)) {
            hexa = "#ffffffff";
        }
        color= Color.parseColor(hexa);
        return color;
    }

    public int setFromHSV(float hue, float saturation, float value){
        color = android.graphics.Color.HSVToColor(new float[]{ hue, saturation/100 , value/100 });
        return color;
    }

    public int setFromHSL(float hue, float saturation, float lightness){
        float[] hsv = HSLtoHSV(hue, saturation/100, lightness/100);
        color = android.graphics.Color.HSVToColor(hsv);
        return color;
    }

    public static float[] HSVtoHSL(float hv, float sv, float v){
        float hl = hv, sl;
        float l = v * (1 - (sv/2));
        if (l==0 || l ==1) sl = 0;
        else sl = (v-l)/(Math.min(l, 1-l));
        return new float[] {hl, sl, l};
    }

    public static float[] HSLtoHSV(float hl, float sl, float l){
        float hv = hl, sv;
        float v = l + sl*Math.min(l,1-l);
        if (v==0) sv = 0;
        else sv = 2*(1-(l/v));
        return new float[] {hv, sv, v};
    }

    public String getStringHSL(){
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);
        float[] hsl = HSVtoHSL(hsv[0], hsv[1], hsv[2]);
        return ""+String.format("%,.0f", hsl[0])+", "+ String.format("%,.0f", hsl[1]*100) +"%, "+String.format("%,.0f", hsl[2]*100)+"%";
    }


    public String getStringHSV(){
        float[] f = new float[3];
        android.graphics.Color.colorToHSV(color, f);
        return ""+String.format("%,.0f", f[0])+", "+String.format("%,.0f", f[1]*100)+"%, "+String.format("%,.0f", f[2]*100)+"%";
    }

    public String getHexString(){
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public String getHexStringWithAlpha(){
        return String.format("#%08X", (color));
    }

    public String getStringRGB(){
        return ""+getRed()+", "+getGreen()+", "+getBlue();
    }

    public int getAlpha(){
        return android.graphics.Color.alpha(color);
    }

    public int getRed(){
        return android.graphics.Color.red(color);
    }

    public int getBlue(){
        return android.graphics.Color.blue(color);
    }
    public int getGreen(){
        return android.graphics.Color.green(color);
    }

    /*
    public String ColorToHex() {
        int alpha = android.graphics.Color.alpha(color);
        int blue = android.graphics.Color.blue(color);
        int green = android.graphics.Color.green(color);
        int red = android.graphics.Color.red(color);

        String alphaHex = To00Hex(alpha);
        String blueHex = To00Hex(blue);
        String greenHex = To00Hex(green);
        String redHex = To00Hex(red);

        // hexBinary value: aabbggrr
        StringBuilder str = new StringBuilder("#");
        str.append(alphaHex);
        str.append(blueHex);
        str.append(greenHex);
        str.append(redHex );

        return str.toString();
    }

    private static String To00Hex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        return hex.substring(hex.length()-2, hex.length());
    }
    */

}

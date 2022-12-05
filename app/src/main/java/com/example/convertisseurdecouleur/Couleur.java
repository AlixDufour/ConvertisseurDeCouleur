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
        color= Color.parseColor("#"+hexa);
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

    public float getHslH(){
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);
        float[] hsl = HSVtoHSL(hsv[0], hsv[1], hsv[2]);
        return hsl[0];
    }

    public float getHslS(){
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);
        float[] hsl = HSVtoHSL(hsv[0], hsv[1], hsv[2]);
        return hsl[1]*100;
    }

    public float getHslL(){
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);
        float[] hsl = HSVtoHSL(hsv[0], hsv[1], hsv[2]);
        return hsl[2]*100;
    }

    public String getStringHSV(){
        float[] f = new float[3];
        android.graphics.Color.colorToHSV(color, f);
        return ""+String.format("%,.0f", f[0])+", "+String.format("%,.0f", f[1]*100)+"%, "+String.format("%,.0f", f[2]*100)+"%";
    }

    public float getHsvH(){
        float[] f = new float[3];
        android.graphics.Color.colorToHSV(color, f);
        return (int) f[0];
    }

    public float getHsvS(){
        float[] f = new float[3];
        android.graphics.Color.colorToHSV(color, f);
        return f[1]*100;
    }

    public float getHsvV(){
        float[] f = new float[3];
        android.graphics.Color.colorToHSV(color, f);
        return f[2]*100;
    }


    public String getHexString(){
        return String.format("%06X", (0xFFFFFF & color));
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

}
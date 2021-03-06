package com.android.pokemonillustratedhandbook.utils;

// dp，sp，px单位转换

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;


public class DisplayUtils {

    public static DisplayMetrics getMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    //获取屏幕密度
    public static float getDensity() {
        return getMetrics().density;
    }

    //获取屏幕高度像素
    public static int getScreenHeight() {
        return getMetrics().heightPixels;
    }

    //获取屏幕宽度像素
    public static int getScreenWidth() {
        return getMetrics().widthPixels;
    }

    //获取屏幕密度DPI
    public static int getDensityDpi() {
        return getMetrics().densityDpi;
    }

    public static String getScreenSize(Context context) {
        String screenSizeText = "";

        int screenSize = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            screenSizeText = "small";
        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            screenSizeText = "normal";
        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            screenSizeText = "large";
        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            screenSizeText = "xlarge";
        }

        return screenSizeText;
    }

    public static String getScreenDensity() {
        String screenDensityText = "";

        int screenDensity = getDensityDpi();
        if (DisplayMetrics.DENSITY_LOW == screenDensity) {
            screenDensityText = "ldpi";
        } else if (DisplayMetrics.DENSITY_MEDIUM == screenDensity) {
            screenDensityText = "mdpi";
        } else if (DisplayMetrics.DENSITY_TV == screenDensity) {
            screenDensityText = "tvdpi";
        } else if (DisplayMetrics.DENSITY_HIGH == screenDensity) {
            screenDensityText = "hdpi";
        } else if (DisplayMetrics.DENSITY_280 == screenDensity) {
            screenDensityText = "280dpi";
        } else if (DisplayMetrics.DENSITY_XHIGH == screenDensity) {
            screenDensityText = "xhdpi";
        } else if (DisplayMetrics.DENSITY_360 == screenDensity) {
            screenDensityText = "360dpi";
        } else if (DisplayMetrics.DENSITY_400 == screenDensity) {
            screenDensityText = "400dpi";
        } else if (DisplayMetrics.DENSITY_420 == screenDensity) {
            screenDensityText = "420dpi";
        } else if (DisplayMetrics.DENSITY_XXHIGH == screenDensity) {
            screenDensityText = "xxhdpi";
        } else if (DisplayMetrics.DENSITY_560 == screenDensity) {
            screenDensityText = "560dpi";
        } else if (DisplayMetrics.DENSITY_XXXHIGH == screenDensity) {
            screenDensityText = "xxxhdpi";
        }
        return screenDensityText;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / getMetrics().density + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * getMetrics().density + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / getMetrics().density + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * getMetrics().density + 0.5f);
    }

    /**
     * 将传进来的数转化为dp
     */
    public static int convertToDp(int num) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, getMetrics());
    }
}

package com.example.blurshapedemo.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.annotation.XmlRes;
import androidx.core.content.ContextCompat;

import java.io.InputStream;

/**
 * author:  zhoucx
 * date:    2020/6/12
 * explain:
 */
public class ResourcesUtils {
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    private static Context getContext() {
        return sContext;
    }

    public static int getColor(@ColorRes int id) {
        return getColor(getContext(), id);
    }

    public static int getColor(@NonNull Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return getDrawable(getContext(), id);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public static String getString(@NonNull Context context, @StringRes int id) {
        return context.getString(id);
    }

    public static String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    public static String getString(@NonNull Context context, @StringRes int id, Object... formatArgs) {
        return context.getString(id, formatArgs);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return getString(getContext(), id, formatArgs);
    }

    public static String[] getStringArray(@NonNull Context context, @ArrayRes int id) {
        return context.getResources().getStringArray(id);
    }

    public static ColorStateList getColorStateList(@ColorRes int id) {
        return getColorStateList(getContext(), id);
    }

    public static ColorStateList getColorStateList(Context context, @ColorRes int id) {
        ColorStateList colorStateList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorStateList = context.getResources().getColorStateList(id, null);
        } else {
            colorStateList = context.getResources().getColorStateList(id);
        }
        return colorStateList;
    }

    public static XmlResourceParser getXml(@XmlRes int id) {
        return getXml(getContext(), id);
    }

    public static XmlResourceParser getXml(Context context, @XmlRes int id) {
        return context.getResources().getXml(id);
    }

    public static InputStream getRawResource(Context context, @RawRes int id) {
        return context.getResources().openRawResource(id);
    }

    public static InputStream getRawResource(@RawRes int id) {
        return getRawResource(getContext(), id);
    }


}

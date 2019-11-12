package com.ggg.common.ui.utils;

import android.content.Context;
import android.graphics.Color;

import com.ggg.common.GGGAppInterface;
import com.ggg.common.utils.LanguageManager;

/**
 * Created by tuannguyen on 10/26/17.
 */

public class ResourceUtil {

    public static int getColor(int colorId){
        Context ctx = GGGAppInterface.gggApp.getCtx();
        if (ctx != null) {
            return ctx.getResources().getColor(colorId);
        }
        return Color.LTGRAY;
    }

    public static String getString(int resource){
        Context ctx = GGGAppInterface.gggApp.getCtx();
        if (ctx != null){
            return LanguageManager.getValue(ctx.getString(resource));
        }
        return "Error";
    }
    public static String[] getArrayString(int resource){
        Context ctx = GGGAppInterface.gggApp.getCtx();
        if (ctx != null){
            String[] types = ctx.getResources().getStringArray(resource);
            String[] resultString = new String[types.length];
            for (int i=0;i<types.length;i++){
                resultString[i] = LanguageManager.getValue(types[i]);
            }
            return resultString;
        }
        return new String[]{""};
    }
}

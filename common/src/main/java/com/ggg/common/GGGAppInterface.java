package com.ggg.common;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

/**
 * Created by tuannguyen on 12/18/17.
 */

public class GGGAppInterface {
    public interface AppInterface {
        public Context getCtx();
        public CircularProgressDrawable getCircularProgressDrawable();
    }
    public static AppInterface gggApp = null;
    public static void initInstance(AppInterface appinterface)
    {
        gggApp = appinterface;
    }
}

package com.ggg.common;

import android.content.Context;

/**
 * Created by tuannguyen on 12/18/17.
 */

public class GGGAppInterface {
    public interface AppInterface {
        public Context getCtx();
    }
    public static AppInterface gggApp = null;
    public static void initInstance(AppInterface appinterface)
    {
        gggApp = appinterface;
    }
}

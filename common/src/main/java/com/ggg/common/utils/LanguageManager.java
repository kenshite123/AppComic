package com.ggg.common.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ggg.common.GGGAppInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuannguyen on 9/19/17.
 */

public class LanguageManager {
    //region Properties
    private static final String TAG = "LanguageManager";
    static final String DEFAULT_LANG_NAME = "English";
    static final String ENG_LANG = "eng";
    Context mContext;
    static LanguageManager instance;
    Map<String, String> dict = new HashMap<>();
    //endregion

    //region constructor
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
            instance.mContext = GGGAppInterface.gggApp.getCtx();
        }
        return instance;
    }

//    public void loadLanguage(List<Mobile> list) {
//        dict = new HashMap<>();
//        $.each(list, obj -> {
//            dict.put(obj.getName(), obj.getText());
//        });
//    }

    public static void reloadLanguage(Fragment fragment) {
        ArrayList<View> views = getAllChildren(fragment.getView());
        for (View view : views) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                if (editText.getHint() != null) {
                    editText.setHint(getValue(editText.getHint().toString()+""));
                }

            } else if (view instanceof android.widget.Button) {
                Button button = (Button) view;
                if (button.getText() != null) {
                    button.setText(getValue(button.getText().toString()));
                }
            } else if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (textView.getText() != null){
                    textView.setText(getValue(textView.getText().toString()));
                }
            }
        }
    }

    public static String getValue(String value) {
        if (LanguageManager.getInstance().dict != null) {
            String result = LanguageManager.getInstance().dict.get(value);
            if (result != null && !result.isEmpty()) {
                return result;
            }
        }
        return value;
    }

    public static String getValue(int resId) {
        try {
            return getValue(instance.mContext.getString(resId));
        } catch (Exception e) {
            return null;
        }
    }

    private static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
    //endregion
}

package com.ggg.common.utils;

/**
 * Created by vihuynh on 8/28/17.
 */

public class StringUtil {

    public static boolean isEmpty(String etText) {
        if (null != etText
                    && etText.toString().trim().length() > 0)
            return false;
        return true;
    }

    public static String toStringObject(Object text){
        if(null != text) {
            return text.toString();
        } else {
            return "";
        }
    }
    public static Double toDoubleString(String etText){
        Double result = null;
        try{
            Double aDouble = Double.valueOf(etText);
            result = aDouble;
        }catch (Exception ex){

        }
        return result;
    }

    public static String capitalize(String str) {
        String[] words = str.split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if(i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }
}

package com.example.yoavgross.rxexmaple;

/**
 * Created by yoavgross on 25/01/2018.
 */

public class DataUtil {

    public static boolean isNullOrEmpty(String text) {
        return text == null;
    }

    public static boolean isNotNullOrEmpty(String text) {
        return !isNullOrEmpty(text);
    }
}

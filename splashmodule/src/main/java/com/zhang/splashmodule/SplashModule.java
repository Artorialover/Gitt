package com.zhang.splashmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashModule {

    private static boolean sm_loadLocal=true;
    private static boolean sm_showBtnSkip=false;
    private static boolean sm_showGuidePage=true;
    private static Class<? extends AppCompatActivity> sm_nextActivity=null;
    private static String uri;

    public static void init(boolean showGuidePage, @NonNull Class<? extends AppCompatActivity> nextActivity){
        sm_showGuidePage=showGuidePage;
        sm_nextActivity=nextActivity;
    }

    public static void loadFromLocal(){
        sm_loadLocal=true;
    }

    public static void loadFromURI(String destUri){
        sm_loadLocal=false;
        uri=destUri;
    }

    public static void showSkipBtn(boolean show){
        sm_showBtnSkip=show;
    }

    public static boolean isLoadLocal() {
        return sm_loadLocal;
    }

    public static boolean isShowBtnSkip() {
        return sm_showBtnSkip;
    }

    public static boolean isShowGuidePage() {
        return sm_showGuidePage;
    }

    public static String getUri() {
        return uri;
    }

    public static Class<? extends AppCompatActivity> getNextActivity() {
        return sm_nextActivity;
    }
}

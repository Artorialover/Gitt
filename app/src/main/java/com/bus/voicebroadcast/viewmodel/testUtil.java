package com.bus.voicebroadcast.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class testUtil {
    public static LiveData<String> getTest(int value){
        MutableLiveData<String> result=new MutableLiveData<>();
        result.postValue(Integer.toString(value));
        return result;
    }
}

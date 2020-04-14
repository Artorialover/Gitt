package com.bus.voicebroadcast.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class SeatViewModel extends ViewModel {

    private final MutableLiveData<Integer> counter=new MutableLiveData<>();
    private final LiveData<String> _counter= Transformations.map(counter, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {

            return "input: "+input+"test";
        }
    });

    private MutableLiveData<Integer> userId=new MutableLiveData<>();
    private LiveData<String> switchMap=Transformations.switchMap(userId, new Function<Integer, LiveData<String>>() {
        @Override
        public LiveData<String> apply(Integer input) {

            return testUtil.getTest(input);
        }
    });

    public void getUserForTest(int id){
        userId.postValue(id);
    }

    public LiveData<String> getUserObserver(){
        return switchMap;
    }


    public SeatViewModel(int count) {
        counter.setValue(count);
    }

    public MutableLiveData<Integer> getCounter() {
        return counter;
    }

    public LiveData<String> get_counter() {
        return _counter;
    }

    public void plusOne(){
        Integer value = counter.getValue();
        if(value==null){
            counter.setValue(0);
        }else {
            counter.setValue(value+1);
        }
    }







}

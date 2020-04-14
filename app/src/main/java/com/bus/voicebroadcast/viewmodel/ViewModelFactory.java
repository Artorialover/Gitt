package com.bus.voicebroadcast.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private int countReserved;
    public ViewModelFactory(int countReserved) {
        this.countReserved=countReserved;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new SeatViewModel(countReserved);
    }
}

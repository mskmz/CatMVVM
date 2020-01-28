package com.mskmz.catmvvm.core.manager;

import androidx.databinding.Observable;

public interface SupportShard {
    void addCallbackLink(Observable.OnPropertyChangedCallback callback);
}

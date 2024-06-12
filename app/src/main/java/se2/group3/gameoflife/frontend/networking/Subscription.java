package se2.group3.gameoflife.frontend.networking;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.Disposable;

public final class Subscription <T> {

    private final Disposable disposable;
    private final MutableLiveData<T> mutableLiveData;

    public Subscription(Disposable disposable, MutableLiveData<T> mutableLiveData) {
        this.disposable = disposable;
        this.mutableLiveData = mutableLiveData;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public MutableLiveData<T> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setLiveDataValue(T value) {
        mutableLiveData.setValue(value);
    }
}
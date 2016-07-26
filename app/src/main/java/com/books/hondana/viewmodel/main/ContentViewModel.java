package com.books.hondana.viewmodel.main;

import android.databinding.ObservableField;

public class ContentViewModel {

    public final ObservableField<String> message = new ObservableField<>();

    public ContentViewModel() {
        message.set("Hello, data binding!");
    }

    public void setMessage(String message) {
        this.message.set(message);
    }
}

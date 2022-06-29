package com.eme22.applicacioncomida.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.User;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> current = new MutableLiveData<>();

    public void setCurrent(User item) {
        current.setValue(item);
    }
    public LiveData<User> getCurrent() {return current;}
}
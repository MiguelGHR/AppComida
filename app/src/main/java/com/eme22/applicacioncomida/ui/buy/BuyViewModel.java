package com.eme22.applicacioncomida.ui.buy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.model.Item;

public class BuyViewModel extends ViewModel {
    private final MutableLiveData<Item> current = new MutableLiveData<>();

    public void setCurrent(Item item) {
        current.setValue(item);
        price.setValue(item.getPrice());
    }
    public LiveData<Item> getCurrent() {return current;}

    private final MutableLiveData<Integer> items = new MutableLiveData<>(1);
    private final MutableLiveData<Double> price = new MutableLiveData<>();

    public LiveData<Double> getPrice() {return price;}

    private void setItems(Integer item) {
        items.setValue(item);
        price.setValue(item * current.getValue().getPrice());
    }
    public LiveData<Integer> getCount() {return items;}

    public void addItem() {
        setItems(items.getValue()+1);
    }

    public void removeItem() {
        if (items.getValue() >= 2)
            setItems(items.getValue()-1);
    }
}
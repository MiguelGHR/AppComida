package com.eme22.applicacioncomida.ui.category_item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryItemViewModel extends ViewModel {


    private final MutableLiveData<ArrayList<Item>> categories = new MutableLiveData<>();

    private final WebApiService service = WebApiAdapter.getApiService();


    LiveData<ArrayList<Item>> getItems() {
        return categories;
    }

    private final MutableLiveData<Item> selected = new MutableLiveData<>();

    public LiveData<Item> getSelected() {return selected;}

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void  retrieveCategoryItems(int categoryId) {

        executor.execute(() -> {

            try {
                Response<ArrayList<Item>> itemResponse = service.itemsByCategory(categoryId).execute();
                if (itemResponse.isSuccessful()) {
                    ArrayList<Item> items = itemResponse.body();
                    if (items != null) {
                        for (int i = 0; i < items.size(); i++) {
                            Item item = items.get(i);
                            if (item.getPromoId() != null && item.getPromo() == null) {
                                Response<Promo> promoResponse = service.getPromo(Math.toIntExact(item.getPromoId())).execute();
                                if (promoResponse.isSuccessful())
                                    item.setPromo(promoResponse.body());
                            }
                        }
                    }


                    categories.postValue(items);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    public void setSelected(Item selected) {
        this.selected.setValue(selected);
    }
}
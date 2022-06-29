package com.eme22.applicacioncomida.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>();

    private final WebApiService service = WebApiAdapter.getApiService();

    private final MutableLiveData<Category> selected = new MutableLiveData<>();

    LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    public LiveData<Category> getSelected() {return selected;}

    public void  retrieveCategories() {
        Call<ArrayList<Category>> promoResponse = service.categories();

        promoResponse.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {

                if (response.isSuccessful())
                    categories.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });

    }

    public void setSelected(Category item) {
        selected.setValue(item);
    }

}
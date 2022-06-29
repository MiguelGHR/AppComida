package com.eme22.applicacioncomida.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.SearchResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Promo>> promos = new MutableLiveData<>();

    private final WebApiService service = WebApiAdapter.getApiService();

    LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    LiveData<ArrayList<Promo>> getPromos() {
        return promos;
    }

    private final MutableLiveData<ArrayList<SearchResult>> search = new MutableLiveData<>();

    LiveData<ArrayList<SearchResult>> getSearch() {
        return search;
    }

    public void retrievePromos() {
        Call<ArrayList<Promo>> promoResponse = service.promos();

        promoResponse.enqueue(new Callback<ArrayList<Promo>>() {
            @Override
            public void onResponse(Call<ArrayList<Promo>> call, Response<ArrayList<Promo>> response) {

                if (response.isSuccessful())
                    promos.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Promo>> call, Throwable t) {

            }
        });

    }

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

    public void retrieveSearch(String query) {
        service.search(query).enqueue(new Callback<ArrayList<SearchResult>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchResult>> call, Response<ArrayList<SearchResult>> response) {
                if (response.isSuccessful()) {
                    search.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SearchResult>>call, Throwable t) {

            }
        });
    }
}
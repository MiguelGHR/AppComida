package com.eme22.applicacioncomida.ui.user_history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Cart;
import com.eme22.applicacioncomida.data.model.Category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHistoryViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Cart>> carts = new MutableLiveData<>();

    LiveData<ArrayList<Cart>> getCarts() {
        return carts;
    }

    private final WebApiService service = WebApiAdapter.getApiService();

    public void retrieveCarts(int userId) {

        service.userCarts(userId).enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {

                if (response.isSuccessful()) {
                    carts.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

            }
        });

    }

}
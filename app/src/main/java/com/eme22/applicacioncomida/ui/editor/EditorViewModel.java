package com.eme22.applicacioncomida.ui.editor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>();

    public LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    private final MutableLiveData<ArrayList<Promo>> promos = new MutableLiveData<>();

    public LiveData<ArrayList<Promo>> getPromos() {
        return promos;
    }

    private final MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();

    LiveData<ArrayList<Item>> getItems() {
        return items;
    }

    private final MutableLiveData<ArrayList<User>> users = new MutableLiveData<>();

    LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    private final WebApiService service = WebApiAdapter.getApiService();


    public void retrieveCategories(){
        service.categories().enqueue(new Callback<ArrayList<Category>>() {
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

    public void retrievePromos(){
        service.promos().enqueue(new Callback<ArrayList<Promo>>() {
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

    public void retrieveItems(){
        service.items().enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                if (response.isSuccessful())
                    items.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {

            }
        });
    }

    public void retrieveUsers(){
        service.users().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful())
                    users.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    public void deleteItem(Object item, OnDeleteCallBack callBack) {
        if (item instanceof Category) {
            service.deleteCategory(Math.toIntExact(((Category) item).getId())).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        retrieveCategories();
                        callBack.onSuccess();
                    }
                    else {
                        System.out.println(response.code());
                        System.out.println(response.errorBody());
                        callBack.onError(new Throwable());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        }
        else if (item instanceof Item) {
            service.deleteItem(Math.toIntExact(((Item) item).getId())).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        retrieveItems();
                        callBack.onSuccess();
                    }

                    else {
                        System.out.println(response.code());
                        System.out.println(response.errorBody());
                        callBack.onError(new Throwable());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        }
        else if (item instanceof User) {
            service.deleteUser(Math.toIntExact(((User) item).getId())).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        retrieveUsers();
                        callBack.onSuccess();
                    }
                    else {
                        System.out.println(response.code());
                        System.out.println(response.errorBody());
                        callBack.onError(new Throwable());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        }


    }

    public interface OnDeleteCallBack {
        public void onSuccess();
        public void onError(Throwable e);
    }
}
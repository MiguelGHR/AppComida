package com.eme22.applicacioncomida.ui.editor_item;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.SearchResult;
import com.eme22.applicacioncomida.data.model.User;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorItemViewModel extends ViewModel {

    private final WebApiService service = WebApiAdapter.getApiService();

    private final MutableLiveData<Boolean> result = new MutableLiveData<>();

    LiveData<Boolean> getResult() {return result;}


    public void submit(User user, File file, boolean NEW_ITEM) {

        if (NEW_ITEM) {
            service.uploadUser(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getAddress(),
                    Math.toIntExact(user.getPhone()),
                    user.isAdmin(),
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*")))
            ).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    result.setValue(response.isSuccessful());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();
                }
            });
        } else {
            service.updateUser(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getAddress(),
                    Math.toIntExact(user.getPhone()),
                    user.isAdmin(),
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*")))
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    result.setValue(response.isSuccessful());

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();
                }
            });
        }
    }

    public void submit(Category user, File file, boolean NEW_ITEM) {

        if (NEW_ITEM) {
            service.uploadCategories(user.getName(),
                    user.getDescription(),
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*")))).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    result.setValue(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();
                }
            });
        }
        else {
            service.updateCategories(
                    Math.toIntExact(NEW_ITEM ? 0 : user.getId()),
                    user.getName(),
                    user.getDescription(),
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*"))
            )).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    result.setValue(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();
                }
            });
        }

    }

    public void submit(Promo promo, File file, boolean NEW_ITEM) {

        if (NEW_ITEM) {
            service.uploadPromo(
                    promo.getName(),
                    promo.getDescription(),
                    promo.getDiscount(),
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*"))
                    )).enqueue(new Callback<Promo>() {
                @Override
                public void onResponse(Call<Promo> call, Response<Promo> response) {
                    result.setValue(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<Promo> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();
                }
            });
        } else {
            service.updatePromo(
                    Math.toIntExact(promo.getId()),
                    promo.getName(),
                    promo.getDescription(),
                    promo.getDiscount(),
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*"))
                    )).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    result.setValue(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();
                }
            });
        }



    }

    public void submit(Item item, File file, boolean NEW_ITEM) {

        if (NEW_ITEM) {
            service.uploadItem(
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    Math.toIntExact(item.getCategoryId()),
                    item.getPromoId() != null ? Math.toIntExact(item.getPromoId()) : null,
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*")))
            ).enqueue(new Callback<Item>() {
                @Override
                public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                    result.setValue(response.isSuccessful());
                }

                @Override
                public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();

                }
            });
        } else {
            service.updateItem(
                    Math.toIntExact(item.getId()),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    Math.toIntExact(item.getCategoryId()),
                    item.getPromoId() != null ? Math.toIntExact(item.getPromoId()) : null,
                    file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*")))
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    result.setValue(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(false);
                    t.printStackTrace();

                }
            });
        }



    }
}
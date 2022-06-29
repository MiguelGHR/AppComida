package com.eme22.applicacioncomida.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Login;
import com.eme22.applicacioncomida.data.model.User;

import java.io.File;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<RegisterFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<RegisterResult> loginResult = new MutableLiveData<>();

    private final WebApiService service = WebApiAdapter.getApiService();

    LiveData<RegisterFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<RegisterResult> getLoginResult() {
        return loginResult;
    }

    public void register(String name, String lastName, String address, Integer phone, String username,String password , File file ) {
        // can be launched in a separate asynchronous job

        Call<Login> response = service.count();

        response.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCount() != null) {
                    int count = response.body().getCount();
                    Call<User> response2 =  service.uploadUser(name, lastName, username, toBase64(password), address, phone, count == 0,
                            file == null ? null : MultipartBody.Part.createFormData("data", file.getName(),
                                    RequestBody.create(file, MediaType.parse("image/*"))
                            ));

                    response2.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                loginResult.setValue(new RegisterResult(response.body()));
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }

    private String toBase64(String password) {
        return new String(
                android.util.Base64.encode(password.getBytes(), android.util.Base64.DEFAULT),
                StandardCharsets.UTF_8
        );

    }

    public void loginDataChanged(String name, String lastName, String address, String phone, String username,String password) {
        if (name.isEmpty() || name.trim().isEmpty()) {
            loginFormState.setValue(new RegisterFormState(R.string.invalid_name, null, null, null, null, null));
        }
        else if (lastName.isEmpty() || lastName.trim().isEmpty()) {
            loginFormState.setValue(
                    new RegisterFormState(null, R.string.invalid_lastName, null, null,  null, null)
            );
        }
        else if (address.isEmpty() || address.trim().isEmpty()) {
            loginFormState.setValue(
                    new RegisterFormState(null, null, null, R.string.invalid_address,  null, null)
            );
        }
        else if (!isNumberValid(phone)) {
            loginFormState.setValue(
                    new RegisterFormState(null, null, R.string.invalid_phone, null,  null, null)
            );
        }

        else if (!isUserNameValid(username)) {
            loginFormState.setValue(
                    new RegisterFormState(null, null, null, null,  R.string.invalid_username, null)
            );
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(
                    new RegisterFormState(null, null, null, null,  null, R.string.invalid_password)
            );
        } else {
            loginFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isNumberValid(String number ) {

        try {
            Integer.parseInt(number);
        } catch (NumberFormatException ignore) {
            return false;
        }

         return number.length() == 9;


    }
}
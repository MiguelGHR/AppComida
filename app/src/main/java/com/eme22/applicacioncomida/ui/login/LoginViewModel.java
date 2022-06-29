package com.eme22.applicacioncomida.ui.login;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Login;
import com.eme22.applicacioncomida.data.model.User;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private final WebApiService service = WebApiAdapter.getApiService();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }



    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Call<User> result = service.getLogin(username, toBase64(password));

        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if (response.isSuccessful()) {
                    User user = response.body();
                    loginResult.setValue(new LoginResult(user));
                    return;
                }

                Call<Login> response2 = service.exist(username);

                response2.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {

                        if (response.isSuccessful()) {
                            Login login = response.body();

                            if (login.getExist()) {
                                loginResult.setValue(new LoginResult(1));
                                return;
                            }
                        }

                        loginResult.setValue(new LoginResult(2));

                    }

                    @Override
                    public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                        t.fillInStackTrace();
                    }
                });



            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Call<Login> response2 = service.exist(username);

                System.out.println("CALLING SERVICE");
                response2.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {

                        if (response.isSuccessful()) {
                            Login login = response.body();

                            if (login != null  && Boolean.TRUE.equals(login.getExist())) {
                                loginResult.setValue(new LoginResult(1));
                                System.out.println("USER NOT");
                                return;
                            }
                        }

                        loginResult.setValue(new LoginResult(2));

                        System.out.println("LOGIN ERROR");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                        t.fillInStackTrace();
                    }
                });
            }
        });





    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
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

    private String toBase64(String string) {
        return new String(
                android.util.Base64.encode(string.getBytes(), android.util.Base64.DEFAULT),
                StandardCharsets.UTF_8
        );
    }
}
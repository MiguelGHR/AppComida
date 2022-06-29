package com.eme22.applicacioncomida.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.databinding.ActivityLoginBinding;
import com.eme22.applicacioncomida.ui.main.MainActivity;
import com.eme22.applicacioncomida.ui.register.RegisterActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "com.eme22.applicacioncomida.USER";
    public static final String EXTRA_USERNAME = "com.eme22.applicacioncomida.USERNAME";
    public static final String EXTRA_PASSWORD = "com.eme22.applicacioncomida.USER";

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    private Boolean isReady  = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        createFakeTimer();
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final EditText username = binding.loginUsername;
        final EditText password = binding.loginPassword;
        final Button login = binding.login;
        final ProgressBar loading = binding.loginLoading;

        final View content = findViewById(android.R.id.content);

        content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isReady) {
                    // The content is ready; start drawing.
                    content.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                } else {
                    // The content is not ready; suspend.
                    return false;
                }
            }
        });

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            login.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                username.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                password.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {

            if (loginResult == null) {
               return;
            }
            loading.setVisibility(View.GONE);

                if (loginResult.getError() != null) {

                    switch (loginResult.getError()) {
                        case 3: noServiceConnection(); return;
                        case 2: promptRegister(username.getText().toString(), password.getText().toString()); return;
                        default: showLoginFailed(); return;
                    }

                }
                if (loginResult.getSuccess() != null) {
                    goMainActivity(loginResult.getSuccess());
                }


        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(username.getText().toString(),
                        password.getText().toString());
            }
        };
        username.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);
        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loading.setVisibility(View.VISIBLE);
                login.setEnabled(false);
                loginViewModel.login(username.getText().toString(),
                        password.getText().toString());
            }
            return false;
        });

        login.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            loginViewModel.login(username.getText().toString(),
                    password.getText().toString());
        });
    }

    private void noServiceConnection() {
        new AlertDialog.Builder(this)
                .setMessage("No hay coneccion al servicio WEB")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goMainActivity(User user ) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_USER, user);
        startActivity(intent);
        finish();
    }

    private void promptRegister(String username , String password ) {
        new AlertDialog.Builder(this)
                .setMessage("Parece que no estas registrado, ¿Deseas registrate?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra(EXTRA_USERNAME, username);
                    intent.putExtra(EXTRA_PASSWORD, password);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {})
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void createFakeTimer() {
        new Handler().postDelayed(() -> isReady = true, 5000);
    }

    private void showLoginFailed() {

        new AlertDialog.Builder(this)
                .setMessage("La contraseña que has ingresado es incorrecta")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {})
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
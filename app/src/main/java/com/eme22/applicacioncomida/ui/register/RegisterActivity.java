package com.eme22.applicacioncomida.ui.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.databinding.ActivityRegisterBinding;
import com.eme22.applicacioncomida.ui.login.LoginActivity;
import com.eme22.applicacioncomida.ui.main.MainActivity;
import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel loginViewModel;
    private ActivityRegisterBinding binding;

    private static final int REQUEST_CODE = 200;
    private File image2 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this)
                .get(RegisterViewModel.class);

        EditText firstName = binding.registerPersonName;
        EditText lastName = binding.registerLastName;
        EditText number = binding.registerPhone;
        EditText address = binding.registerAddress;
        EditText username = binding.registerUsername;
        EditText password = binding.registerPassword;
        Button login = binding.register;
        ProgressBar loading = binding.registerLoading;
        ImageButton image = binding.registerImageName;

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }

            login.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getNameError() != null) {
                firstName.setError(getString(loginFormState.getNameError()));
            }
            if (loginFormState.getLastNameError() != null) {
                lastName.setError(getString(loginFormState.getLastNameError()));
            }
            if (loginFormState.getAddressError() != null) {
                address.setError(getString(loginFormState.getAddressError()));
            }
            if (loginFormState.getPhoneError() != null) {
                number.setError(getString(loginFormState.getPhoneError()));
            }

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
                showLoginFailed(loginResult.getError());
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
                loginViewModel.loginDataChanged(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        address.getText().toString(),
                        number.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString()
                );
            }
        };

        firstName.addTextChangedListener(afterTextChangedListener);
        lastName.addTextChangedListener(afterTextChangedListener);
        address.addTextChangedListener(afterTextChangedListener);
        number.addTextChangedListener(afterTextChangedListener);
        username.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);

        TextView.OnEditorActionListener listener = (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && login.isEnabled()) {
                loginViewModel.register(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        address.getText().toString(),
                        Integer.parseInt(number.getText().toString()),
                        username.getText().toString(),
                        password.getText().toString(),
                        image2
                );
            }
            return false;
        };

        firstName.setOnEditorActionListener(listener);
        lastName.setOnEditorActionListener(listener);
        address.setOnEditorActionListener(listener);
        number.setOnEditorActionListener(listener);
        username.setOnEditorActionListener(listener);
        password.setOnEditorActionListener(listener);



        login.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            loginViewModel.register(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    address.getText().toString(),
                    Integer.parseInt(number.getText().toString()),
                    username.getText().toString(),
                    password.getText().toString(),
                    image2
            );
        });

        image.setOnClickListener(v -> openGalleryForImages());
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && null != result) {
                    String selectedImage = null;
                    try {
                        selectedImage = getRealPathFromURI(RegisterActivity.this, result.getData().getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image2 = new File(selectedImage);
                }
            });

    private void openGalleryForImages() {
        Intent chooserIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityIntent.launch(chooserIntent);
    }

    private void goMainActivity(User success) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USER, success);
        startActivity(intent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NewApi")
    private String getRealPathFromURI(Context context, Uri uri) throws IOException {
        Boolean isKitKat =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            //main if start

            // DocumentProvider
            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(context, uri);
            }
            else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider

                String docId = DocumentsContract.getDocumentId(uri);
                String[] split  = docId.split(":");
                String type = split[0];       // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return context.getExternalFilesDir(null).toString()+ "/" + split[1];
                    }
                    else {
                        return context.getExternalFilesDir(null).
                                toString() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }
            }
            else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r", null);
                FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                File file = new File( context.getCacheDir(), getFileName(context.getContentResolver(),uri));
                FileOutputStream outputStream = new FileOutputStream(file);
                IOUtils.copyStream(inputStream, outputStream);
                return file.getPath();
            }
        }
            else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                return copyFileToInternalStorage(context,uri,"app name");

            }
        //main if end
        else if (uri.getScheme().equalsIgnoreCase("content")) {
            // MediaStore (and general)
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            else
                return copyFileToInternalStorage(context, uri, "your app name");
        }
        else if (uri.getScheme().equalsIgnoreCase("file")) {
            // File
            return uri.getPath();
        }
        return null;
    }

    String getFileName(ContentResolver resolver, Uri fileUri) throws UnsupportedEncodingException {
        String name = "";
        Cursor returnCursor = resolver.query(fileUri, null, null, null, null);
        if (returnCursor != null) {
            int nameIndex =returnCursor.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            name = returnCursor.getString(nameIndex);
            returnCursor.close();
        }
        return URLEncoder.encode(name, "utf-8");
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    Boolean isExternalStorageDocument(Uri uri ) {
        return uri.getAuthority().equals("com.android.externalstorage.documents");
    }
    Boolean isGoogleDriveUri(Uri uri) {
        return uri.getAuthority().equals("com.google.android.apps.docs.storage") ||
                uri.getAuthority().equals("com.google.android.apps.docs.storage.legacy");
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    Boolean isDownloadsDocument(Uri uri) {
        return uri.getAuthority().equals("com.android.providers.downloads.documents");
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    Boolean isMediaDocument(Uri uri) {
        return uri.getAuthority().equals("com.android.providers.media.documents");
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    Boolean isGooglePhotosUri(Uri uri ) {
        return uri.getAuthority().equals("com.google.android.apps.photos.content");
    }

    String getDriveFilePath(Context context, Uri uri) throws UnsupportedEncodingException, FileNotFoundException {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);

        int nameIndex  = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        Integer sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name  = (returnCursor.getString(nameIndex));
        File file = new File(
                context.getCacheDir(),
                URLEncoder.encode(name, "utf-8"));
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();
            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int len = inputStream.read(buffer);
            while (len != -1) {
                outputStream.write(buffer, 0, len);
                len = inputStream.read(buffer);
            }

            outputStream.flush();
            outputStream.close();

            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();
    }
    private String copyFileToInternalStorage(Context mContext, Uri uri, String newDirName ) throws UnsupportedEncodingException {
        Cursor returnCursor = mContext.getContentResolver().query(
            uri, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE},
            null,
            null,
            null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        File output;
        if (!Objects.equals(newDirName, ""))
        {
            File dir =new File(mContext.getFilesDir().toString()
                    + "/" + newDirName);
            if (!dir.exists())
            {
                dir.mkdir();
            }
            output = new File(
                    mContext.getFilesDir().toString()
                            + "/" + newDirName + "/"
                            + URLEncoder.encode(name,"utf-8"));
        }
        else
        {
            output = new File(mContext.getFilesDir().toString()
                    + "/" + URLEncoder.encode(name, "utf-8"));
        }
        try
        {
            InputStream inputStream =
                mContext.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = inputStream.read(buffer);
            while (len != -1) {
                outputStream.write(buffer, 0, len);
                len = inputStream.read(buffer);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
        Log.e("Exception", e.getMessage());
    }
        return output.getPath();
    }



}
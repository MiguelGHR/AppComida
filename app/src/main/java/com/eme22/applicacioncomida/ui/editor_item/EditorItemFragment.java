package com.eme22.applicacioncomida.ui.editor_item;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.CATEGORY_EDITOR;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.ITEM_EDITOR;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.PROMO_EDITOR;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.USER_EDITOR;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.ui.editor.EditorViewModel;
import com.eme22.applicacioncomida.databinding.FragmentEditorItemBinding;
import com.google.android.gms.common.util.IOUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditorItemFragment extends Fragment {

    private EditorItemViewModel mViewModel;
    private FragmentEditorItemBinding binding;

    private boolean canceled = false;

    private final Object item;
    private File image = null;

    private final int CURRENT_EDITOR;
    private boolean NEW_ITEM = false;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private EditorViewModel helperViewModel;

    private EditorItemFragment(Object item) {
        this.item = item;
        if (item instanceof Category)  {
            CURRENT_EDITOR = CATEGORY_EDITOR;
        }
        else if (item instanceof Promo)  {
            CURRENT_EDITOR = PROMO_EDITOR;
        }
        else if (item instanceof Item)  {
            CURRENT_EDITOR = ITEM_EDITOR;
        }
        else if (item instanceof User)  {
            CURRENT_EDITOR = USER_EDITOR;
        }
        else if (item instanceof Integer) {
            CURRENT_EDITOR = (int) item;
            NEW_ITEM = true;
        }

        else throw new IndexOutOfBoundsException();
    }

    public static EditorItemFragment newInstance(Object item) {
        return new EditorItemFragment(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEditorItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        fixViewBug();

        initView();
        initData();

        return view;
    }

    private void fixViewBug() {
        ConstraintLayout fl = binding.getRoot();
        fl.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
    }

    private void initView() {
        mViewModel = new ViewModelProvider(requireActivity()).get(EditorItemViewModel.class);
        if (CURRENT_EDITOR == ITEM_EDITOR)
            helperViewModel = new ViewModelProvider(requireActivity()).get(EditorViewModel.class);

        binding.fragmentEditorItemToolbar.setNavigationOnClickListener(v -> onCancel());
        binding.fragmentEditorCancel.setOnClickListener(v -> onCancel());

        binding.fragmentEditorItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryForImages();
            }
        });

        binding.fragmentEditorAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CURRENT_EDITOR == CATEGORY_EDITOR) {

                    Category itemToSend = item instanceof Category ? (Category) item : new Category();

                    System.out.println(itemToSend.getId());
                    itemToSend.setId(itemToSend.getId());
                    itemToSend.setName(binding.fragmentEditorProperty1.getText().toString());
                    itemToSend.setDescription(binding.fragmentEditorProperty2.getText().toString());
                    mViewModel.submit(itemToSend, image, NEW_ITEM);
                }
                else if (CURRENT_EDITOR == PROMO_EDITOR) {

                    Promo itemToSend = item instanceof Promo ? (Promo) item : new Promo();
                    System.out.println(itemToSend.getId());
                    itemToSend.setId(itemToSend.getId());
                    itemToSend.setName(binding.fragmentEditorProperty1.getText().toString());
                    itemToSend.setDescription(binding.fragmentEditorProperty2.getText().toString());
                    itemToSend.setDiscount(Double.parseDouble(binding.fragmentEditorProperty3.getText().toString()));
                    mViewModel.submit(itemToSend, image, NEW_ITEM);

                }
                else if (CURRENT_EDITOR == ITEM_EDITOR) {

                    Item itemToSend = item instanceof Item ? (Item) item : new Item();
                    System.out.println(itemToSend.getId());
                    itemToSend.setId(itemToSend.getId());
                    itemToSend.setName(binding.fragmentEditorProperty1.getText().toString());
                    itemToSend.setDescription(binding.fragmentEditorProperty2.getText().toString());
                    itemToSend.setPrice(Double.parseDouble(binding.fragmentEditorProperty3.getText().toString()));
                    itemToSend.setCategoryId(((Category) binding.fragmentEditorProperty7.getSelectedItem()).getId());
                    itemToSend.setPromoId(binding.fragmentEditorProperty8.getSelectedItemPosition() == 0 ? null : ((Promo) binding.fragmentEditorProperty8.getSelectedItem()).getId());

                    mViewModel.submit(itemToSend, image, NEW_ITEM);

                }
                else if ( CURRENT_EDITOR == USER_EDITOR) {

                    User itemToSend = item instanceof User ? (User) item : new User();
                    System.out.println(itemToSend.getId());
                    itemToSend.setId(itemToSend.getId());
                    itemToSend.setFirstName(binding.fragmentEditorProperty1.getText().toString());
                    itemToSend.setLastName(binding.fragmentEditorProperty2.getText().toString());
                    itemToSend.setAddress(binding.fragmentEditorProperty3.getText().toString());
                    itemToSend.setPhone(Long.valueOf(binding.fragmentEditorProperty4.getText().toString()));
                    itemToSend.setEmail(binding.fragmentEditorProperty5.getText().toString());
                    if (!binding.fragmentEditorProperty6.getText().toString().isEmpty()) {
                        itemToSend.setPasswordHash(toBase64(binding.fragmentEditorProperty6.getText().toString()));
                    }

                    itemToSend.setAdmin(binding.fragmentEditorProperty7.getSelectedItemPosition() == 0);

                    mViewModel.submit(itemToSend, image, NEW_ITEM);
                }


                mViewModel.getResult().observe(getViewLifecycleOwner(), aBoolean -> {
                    if (aBoolean) {
                        canceled = true;
                        requireActivity().onBackPressed();
                    }
                });

            }
        });

        if (NEW_ITEM)
            binding.fragmentEditorItemToolbar.setTitle("Nuevo");


        if (CURRENT_EDITOR == CATEGORY_EDITOR) {

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            if (!NEW_ITEM) {
                Picasso.get().load(API_URL + ((Category) item).getImage()).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage,         new Callback() {
                    @Override
                    public void onSuccess() {
                        downLoadImage( API_URL + ((Category) item).getImage());

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
           else
                Picasso.get().load(R.drawable.ic_file_upload).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage);

            binding.fragmentEditorText1.setText("Nombre: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty1.setText(((Category) item).getName());

            binding.fragmentEditorText2.setText("Descripcion: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty2.setText(((Category) item).getDescription());

            binding.fragmentEditorText3.setVisibility(View.GONE);
            binding.fragmentEditorProperty3.setVisibility(View.GONE);

            binding.fragmentEditorText4.setVisibility(View.GONE);
            binding.fragmentEditorProperty4.setVisibility(View.GONE);

            binding.fragmentEditorText5.setVisibility(View.GONE);
            binding.fragmentEditorProperty5.setVisibility(View.GONE);

            binding.fragmentEditorText6.setVisibility(View.GONE);
            binding.fragmentEditorProperty6.setVisibility(View.GONE);

            binding.fragmentEditorText7.setVisibility(View.GONE);
            binding.fragmentEditorProperty7.setVisibility(View.GONE);

            binding.fragmentEditorText8.setVisibility(View.GONE);
            binding.fragmentEditorProperty8.setVisibility(View.GONE);

        }
        else if (CURRENT_EDITOR == PROMO_EDITOR) {

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            if (!NEW_ITEM)
                Picasso.get().load(API_URL + ((Promo) item).getImage()).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage,         new Callback() {
                    @Override
                    public void onSuccess() {
                        downLoadImage( API_URL + ((Promo) item).getImage());

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            else
                Picasso.get().load(R.drawable.ic_file_upload).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage);

            binding.fragmentEditorText1.setText("Nombre: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty1.setText(((Promo) item).getName());

            binding.fragmentEditorText2.setText("Descripcion: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty2.setText(((Promo) item).getDescription());

            binding.fragmentEditorText3.setText("Descuento: S/. ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty3.setText(df.format(((Promo) item).getDiscount()));

            binding.fragmentEditorText4.setVisibility(View.GONE);
            binding.fragmentEditorProperty4.setVisibility(View.GONE);

            binding.fragmentEditorText5.setVisibility(View.GONE);
            binding.fragmentEditorProperty5.setVisibility(View.GONE);

            binding.fragmentEditorText6.setVisibility(View.GONE);
            binding.fragmentEditorProperty6.setVisibility(View.GONE);

            binding.fragmentEditorText7.setVisibility(View.GONE);
            binding.fragmentEditorProperty7.setVisibility(View.GONE);

            binding.fragmentEditorText8.setVisibility(View.GONE);
            binding.fragmentEditorProperty8.setVisibility(View.GONE);


        }
        else if (CURRENT_EDITOR == ITEM_EDITOR) {

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            if (!NEW_ITEM)
                Picasso.get().load(API_URL + ((Item) item).getImage()).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage,         new Callback() {
                    @Override
                    public void onSuccess() {
                        downLoadImage( API_URL + ((Item) item).getImage());

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            else
                Picasso.get().load(R.drawable.ic_file_upload).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage);

            binding.fragmentEditorText1.setText("Nombre: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty1.setText(((Item) item).getName());

            binding.fragmentEditorText2.setText("Descripcion: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty2.setText(((Item) item).getDescription());

            binding.fragmentEditorText3.setText("Precio: S/. ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty3.setText(df.format(((Item) item).getPrice()));

            binding.fragmentEditorText4.setVisibility(View.GONE);
            binding.fragmentEditorProperty4.setVisibility(View.GONE);

            binding.fragmentEditorText5.setVisibility(View.GONE);
            binding.fragmentEditorProperty5.setVisibility(View.GONE);

            binding.fragmentEditorText6.setVisibility(View.GONE);
            binding.fragmentEditorProperty6.setVisibility(View.GONE);


            binding.fragmentEditorText7.setText("Categoria: ");

            binding.fragmentEditorText8.setText("Promocion: ");

        }
        else if ( CURRENT_EDITOR == USER_EDITOR) {

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            if (!NEW_ITEM)
                Picasso.get().load(API_URL + ((User) item).getImage()).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage,         new Callback() {
                    @Override
                    public void onSuccess() {
                        downLoadImage( API_URL + ((User) item).getImage());

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            else
                Picasso.get().load(R.drawable.ic_file_upload).error(R.drawable.ic_file_upload).placeholder(circularProgressDrawable).into(binding.fragmentEditorItemImage);

            binding.fragmentEditorText1.setText("Nombres: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty1.setText(((User) item).getFirstName());

            binding.fragmentEditorText2.setText("Apellidos: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty2.setText(((User) item).getLastName());

            binding.fragmentEditorText3.setText("Direccion: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty3.setText(((User) item).getAddress());

            binding.fragmentEditorText4.setText("Telefono: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty4.setText(String.valueOf(Math.toIntExact(((User) item).getPhone())));

            binding.fragmentEditorText5.setText("Correo: ");
            if (!NEW_ITEM)
                binding.fragmentEditorProperty5.setText(((User) item).getEmail());

            binding.fragmentEditorText6.setText("Contraseña: ");
            binding.fragmentEditorProperty6.setText("");

            binding.fragmentEditorText7.setText("Admin: ");

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.boolean_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.fragmentEditorProperty7.setAdapter(adapter);
            if (!NEW_ITEM)
                binding.fragmentEditorProperty7.setSelection(((User) item).isAdmin() ? 0 : 1);

            binding.fragmentEditorText8.setVisibility(View.GONE);
            binding.fragmentEditorProperty8.setVisibility(View.GONE);

        }
    }

    private void initData() {

        if (CURRENT_EDITOR == ITEM_EDITOR) {
            helperViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {

                CategoryArrayAdapter categoryArrayAdapter = new CategoryArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories);
                categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fragmentEditorProperty7.setAdapter(categoryArrayAdapter);
                if (!NEW_ITEM)
                    binding.fragmentEditorProperty7.setSelection(categoryArrayAdapter.getAdapterPosition(Math.toIntExact(((Item) item).getCategoryId())));

            });
            //helperViewModel.retrieveCategories();

            helperViewModel.getPromos().observe(getViewLifecycleOwner(), categories -> {

                PromoArrayAdapter promoArrayAdapter = new PromoArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories);
                promoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fragmentEditorProperty8.setAdapter(promoArrayAdapter);
            });
            //helperViewModel.retrievePromos();
        }

    }

    private void downLoadImage(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }

                File cacheFile = new File(requireContext().getCacheDir(), "tempImage"+url.lastIndexOf('.'));
                FileOutputStream fos = new FileOutputStream(cacheFile);
                fos.write(response.body().bytes());
                fos.close();
                image = cacheFile;
            }
        });
    }

    public void onCancel() {
        new AlertDialog.Builder(requireContext())
                .setMessage("¿Estas seguro que desea cancelar?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    canceled = true;
                    requireActivity().onBackPressed();
                })
                .setNegativeButton(android.R.string.cancel,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean isCanceled() {
        return !canceled;
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    String selectedImage = null;
                    try {
                        if (result.getData() != null) {
                            selectedImage = getRealPathFromURI(requireContext(), result.getData().getData());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (selectedImage != null) {
                        image = new File(selectedImage);
                        Picasso.get().load(image).into(binding.fragmentEditorItemImage);
                    }
                }
            });

    private void openGalleryForImages() {
        Intent chooserIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityIntent.launch(chooserIntent);
    }

    @SuppressLint("NewApi")
    private String getRealPathFromURI(Context context, Uri uri) throws IOException {

        if (DocumentsContract.isDocumentUri(context, uri)) {
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

                copy(inputStream, outputStream);

                //IOUtils.copyStream(inputStream, outputStream);
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
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);

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
            int maxBufferSize = 1024 * 1024;
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

    void copy(FileInputStream source, FileOutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }

    private String toBase64(String password) {
        return new String(
                android.util.Base64.encode(password.getBytes(), android.util.Base64.DEFAULT),
                StandardCharsets.UTF_8
        );

    }
}
package com.app.justclap.activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.aynctask.CommonAsyncTaskAquery;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.utils.AppUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.CropImage;

public class UserProfile extends AppCompatActivity implements ApiResponse {


    private BroadcastReceiver broadcastReceiver;
    String path = "";
    private File mFileTemp, selectedFilePath;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private Context context;
    private ImageView image_user;
    private Toolbar toolbar;
    private EditText edt_name, edt_email, edt_mobile, edt_address;
    private Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }

        context = this;
        init();
        setListener();

    }

    private void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image_user = (ImageView) findViewById(R.id.image_user);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_address = (EditText) findViewById(R.id.edt_address);
        btn_update = (Button) findViewById(R.id.btn_update);
        AppUtils.fontGotham_edit(edt_address, context);
        AppUtils.fontGotham_edit(edt_email, context);
        AppUtils.fontGotham_edit(edt_mobile, context);
        AppUtils.fontGotham_edit(edt_address, context);
        AppUtils.fontGotham_Button(btn_update, context);

    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidLoginDetails()) {
                    updateProfile();
                }
            }
        });

    }

    private void updateProfile() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userId", AppUtils.getUserId(context));
                hm.put("name", edt_name.getText().toString());
                hm.put("email", edt_email.getText().toString());
                hm.put("address", edt_address.getText().toString());
                hm.put("isVendor", "0");

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.updateProfile);
                new CommonAsyncTaskAquery(1, context, this).getquery(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edt_email.getText().toString();
        String address = edt_address.getText().toString();
        String first_name = edt_name.getText().toString();
        String mobileno = edt_mobile.getText().toString();

        if (!emailaddress.equalsIgnoreCase("") && !first_name.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("") && !address.equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                edt_email.setError(getString(R.string.error_invalid_email));
                edt_email.requestFocus();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                edt_mobile.setError(getString(R.string.mobileno_Length));
                edt_mobile.requestFocus();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_name.setError(getString(R.string.enterFirstName));
                edt_name.requestFocus();
            } else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_email.setError(getString(R.string.enter_email));
                edt_email.requestFocus();
            } else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_mobile.setError(getString(R.string.enterMobileno));
                edt_mobile.requestFocus();
            } else if (address.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_address.setError(getString(R.string.enteraddress));
                edt_address.requestFocus();
            }
        }
        return isValidLoginDetails;
    }

    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePicture();

                } else if (items[item].equals("Choose from Library")) {

                    openGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                    /*
                     * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
		        	 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("color upload 88888", +requestCode + "");
        switch (requestCode) {

            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;

            case REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
                //  upload_image.setText("Image upload successfully");
                break;

            case REQUEST_CODE_CROP_IMAGE:
                try {
                    path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                selectedFilePath = new File(path);
                Log.e("filepath", "**" + selectedFilePath);
                image_user.setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject maindata = commandResult.getJSONObject("data");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}

package com.app.lunavista.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.AppUtils.CircleTransform;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataFileResponse;
import com.app.lunavista.asyntask.AsyncPostDataResponse;
import com.app.lunavista.asyntask.AsyncPostDataResponseNoProgress;
import com.app.lunavista.interfaces.ConnectionDetector;
import com.app.lunavista.interfaces.ListenerPostData;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by Logiguyz on 7/10/2016.
 */
public class UpdateProfile extends AppCompatActivity implements ListenerPostData {

    Context context;
    EditText edtUsername, edtPassword, edtRePassword, edtEmail, edtMobile, edtLocation;
    RelativeLayout rlSignUp;
    ImageView image_profile;
    ConnectionDetector cd;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    String path = "", selectedPath1 = "";
    private ImageView mImageView;
    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        context = this;
        init();
        cd = new ConnectionDetector(context);
        setListener();
        getProfileDetails();
    }

    private void getProfileDetails() {
        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                Toast.makeText(context, "Internet Connection Error, Please connect to working Internet connection", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserId(context)));


                new AsyncPostDataResponse(context, 1, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_getProfile));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void init() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        image_profile = (ImageView) findViewById(R.id.image_profile);
        rlSignUp = (RelativeLayout) findViewById(R.id.rlSignUp);


    }

    private void setListener() {

        rlSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageprofile();
            }
        });


    }

    private void selectImageprofile() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Add Profile Photo!");
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

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode", requestCode + "");


        switch (requestCode) {
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

                break;
            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;
            case REQUEST_CODE_CROP_IMAGE:

                try {

                    path = data.getStringExtra(CropImage.IMAGE_PATH);

                    if (path == null) {

                        return;
                    }
                    File f = new File(path);
                    Picasso.with(context).load(f).transform(new CircleTransform()).into(image_profile);
                    //     bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                    //   image_profile.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // image_profile.setImageBitmap(bitmap);
                break;

        }

    }


    private void updateProfile() {


        try {
            String username = "", password = "", email = "", repassword = "", location = "", mobile = "";
            username = edtUsername.getText().toString();
            email = edtEmail.getText().toString();
            mobile = edtMobile.getText().toString();
            location = edtLocation.getText().toString();

            if (username.length() > 0 && mobile.length() > 0 && location.length() > 0 && email.length() > 0) {

                if (!AppUtils.isEmailValid(email)) {
                    Toast.makeText(context, "Please input valid email.", Toast.LENGTH_LONG).show();
                } else {


                    Charset encoding = Charset.forName("UTF-8");
                    MultipartEntity reqEntity = new MultipartEntity();
                    try {
                        StringBody sb1 = new StringBody(username, encoding);
                        StringBody sb11 = new StringBody(AppUtils.getUserId(context), encoding);
                        StringBody sb111 = new StringBody(mobile, encoding);
                        StringBody sb112 = new StringBody(email, encoding);
                        StringBody sb113 = new StringBody(location, encoding);

                        FileBody filebodyimage = null;
                        selectedPath1 = path;
                        if (!selectedPath1.equalsIgnoreCase("")) {
                            filebodyimage = new FileBody(new File(selectedPath1));
                            reqEntity.addPart("profile_image", filebodyimage);
                        }

                        reqEntity.addPart("user_id", sb11);
                        reqEntity.addPart("name", sb1);
                        reqEntity.addPart("mobile", sb111);
                        reqEntity.addPart("email", sb112);
                        reqEntity.addPart("location", sb113);


                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    if (cd.isConnectingToInternet()) {

                        String url = getString(R.string.url_base)
                                + getString(R.string.url_updateProfile);
                        new AsyncPostDataFileResponse(context, 2, reqEntity, url);

                    } else {

                        Toast.makeText(context, getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();

                    }


                }


            } else {
                Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e)

        {
            Log.e("exception", e.getMessage());
        }
    }


    @Override
    public void onPostRequestSucess(int position, String response) {

        if (position == 1) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");


                    /*"Name":"nitin kumar srivastava",
                            "Email":"nks27011988@gmail.com",
                            "UserRoleId":"2",
                            "UserRole":"Customer",
                            "Phone":"4846646764",
                            "Address":"",
                            "Location":"noida",
                            "Latitude":"28.2100000000000",
                            "Longitude":"78.5400000000000",
                            "IsActive":"1",
                            "ProfileImage":""*/

                    edtUsername.setText(data.getString("Name"));
                    edtEmail.setText(data.getString("Email"));
                    edtMobile.setText(data.getString("Phone"));
                    edtLocation.setText(data.getString("Location"));
                    AppUtils.setUserName(context, data.getString("Name"));
                    AppUtils.setUserEmail(context, data.getString("Email"));
                    AppUtils.setUserImage(context, data.getString("ProfileImage"));

                    if (!data.getString("ProfileImage").equalsIgnoreCase("")) {

                        Picasso.with(context).load(data.getString("ProfileImage"))
                                .placeholder(R.drawable.placeholder).transform(new CircleTransform()).into(image_profile);


                    }


                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (position == 2) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setUserName(context, data.getString("Name"));
                    AppUtils.setUserEmail(context, data.getString("Email"));
                    AppUtils.setUserImage(context, data.getString("ProfileImage"));
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        Toast.makeText(context, "Problem to connect server, please try again.", Toast.LENGTH_LONG).show();
    }
}

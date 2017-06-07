package com.app.justclap.vendorfragments;

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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.activities.InternalStorageContentProvider;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendor.EditVendorBusinessInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.janmuller.android.simplecropimage.CropImage;

public class FragmentBusiness extends Fragment {

    private TextView textadd_agreement, textServiceOfferedValue, textDistance, textDistanceValue, textBusinesslocation, textBusinesslocationValue, textBusinessDescription, textBusinessDescriptionValue, textsocial_presence, textwebsite,
            textwebsiteValue, textfacebook, textfacebookValue, textServiceOffered, textBuisnessNameValue, textBuisnessName;
    private ImageView image_edit, image_add;
    private RecyclerView recycler_agreement;
    private Context context;
    String path = "", selectedPath1 = "";
    private File mFileTemp, selectedFilePath;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_vendor_business, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(context.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        image_edit = (ImageView) view.findViewById(R.id.image_edit);
        image_add = (ImageView) view.findViewById(R.id.image_add);
        textadd_agreement = (TextView) view.findViewById(R.id.textadd_agreement);
        textServiceOfferedValue = (TextView) view.findViewById(R.id.textServiceOfferedValue);
        textDistance = (TextView) view.findViewById(R.id.textDistance);
        textDistanceValue = (TextView) view.findViewById(R.id.textDistanceValue);
        textBusinesslocation = (TextView) view.findViewById(R.id.textBusinesslocation);
        textBusinesslocationValue = (TextView) view.findViewById(R.id.textBusinesslocationValue);
        textBusinessDescription = (TextView) view.findViewById(R.id.textBusinessDescription);
        textBusinessDescriptionValue = (TextView) view.findViewById(R.id.textBusinessDescriptionValue);
        textsocial_presence = (TextView) view.findViewById(R.id.textsocial_presence);
        textwebsite = (TextView) view.findViewById(R.id.textwebsite);
        textwebsiteValue = (TextView) view.findViewById(R.id.textwebsiteValue);
        textfacebook = (TextView) view.findViewById(R.id.textfacebook);
        textfacebookValue = (TextView) view.findViewById(R.id.textfacebookValue);
        textServiceOffered = (TextView) view.findViewById(R.id.textServiceOffered);
        textBuisnessNameValue = (TextView) view.findViewById(R.id.textBuisnessNameValue);
        textBuisnessName = (TextView) view.findViewById(R.id.textBuisnessName);


        AppUtils.fontGotham_Medium(textBuisnessNameValue, context);
        AppUtils.fontGotham_Medium(textfacebookValue, context);
        AppUtils.fontGotham_Medium(textBusinesslocationValue, context);
        AppUtils.fontGotham_Medium(textServiceOfferedValue, context);
        AppUtils.fontGotham_Medium(textBusinessDescriptionValue, context);
        AppUtils.fontGotham_Medium(textDistanceValue, context);
        AppUtils.fontGotham_Medium(textwebsiteValue, context);
        AppUtils.fontGotham_Book(textBuisnessName, context);
        AppUtils.fontGotham_Medium(textadd_agreement, context);
        AppUtils.fontGotham_Book(textServiceOffered, context);
        AppUtils.fontGotham_Book(textfacebook, context);
        AppUtils.fontGotham_Book(textDistance, context);
        AppUtils.fontGotham_Book(textsocial_presence, context);
        AppUtils.fontGotham_Book(textwebsite, context);
        AppUtils.fontGotham_Book(textBusinessDescription, context);
        AppUtils.fontGotham_Book(textBusinesslocation, context);


        recycler_agreement = (RecyclerView) view.findViewById(R.id.recycler_agreement);
        recycler_agreement.setLayoutManager(new GridLayoutManager(context, 3));
        recycler_agreement.setNestedScrollingEnabled(false);

        setListener();
    }

    private void setListener() {

        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditVendorBusinessInfo.class);
                startActivity(intent);
            }
        });
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
                    InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
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
                //     profile_image.setImageBitmap(bitmap);
                break;
        }
    }

}

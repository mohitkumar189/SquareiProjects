package com.app.justclap.vendor_fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.activities.GalleryViewActivity;
import com.app.justclap.activities.InternalStorageContentProvider;
import com.app.justclap.R;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

import com.app.justclap.adapters.AdapterVendorPortfolio;
import com.app.justclap.asyntask.AsyncPostDataFileResponseFragment;
import com.app.justclap.asyntask.AsyncPostDataResponse;

import eu.janmuller.android.simplecropimage.CropImage;

import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.Vendor_homePage;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_VendorPortfolio extends Fragment implements OnCustomItemClicListener, ListenerPostData {

    GridView list_portfolio;
    ModelService serviceDetail;
    Context context;
    RelativeLayout add_porfolio;
    AdapterVendorPortfolio adapterPortfolio;
    ArrayList<ModelService> imagelist;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    String path = "";
    private ImageView mImageView;
    private File mFileTemp;
    TextView text_approval;
    ConnectionDetector cd;
    String selectedPath1 = "";
    Bundle b;
    Vendor_homePage activity;
    String imageID = "";
    int deletedimagePosition;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_vendorportfolio, container, false);
        context = getActivity();
        activity = (Vendor_homePage) getActivity();
        cd = new ConnectionDetector(context);
        imagelist = new ArrayList<>();
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), Fragment_VendorPortfolio.TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getActivity().getFilesDir(), Fragment_VendorPortfolio.TEMP_PHOTO_FILE_NAME);
        }
        text_approval = (TextView) viewCategory.findViewById(R.id.text_approval);

        add_porfolio = (RelativeLayout) viewCategory.findViewById(R.id.rl_porfolio);
        b = getArguments();
        list_portfolio = (GridView) viewCategory.findViewById(R.id.list_portfolio);
        NestedScrollView scrollView = (NestedScrollView) viewCategory.findViewById(R.id.nest_scrollview1);
        scrollView.setFillViewport(true);
        setData();

        add_porfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage1();

            }
        });

        return viewCategory;
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

    private void addPortfolio() {
        Charset encoding = Charset.forName("UTF-8");
        MultipartEntity reqEntity = new MultipartEntity();
        try {
            // StringBody sb1 = new StringBody(emp_name, encoding);
            StringBody sb11 = new StringBody(AppUtils.getvendorId(context), encoding);
            //  StringBody sb111 = new StringBody(id, encoding);
            FileBody filebodyimage = null;
            selectedPath1 = path;
            if (!selectedPath1.equalsIgnoreCase("")) {
                filebodyimage = new FileBody(new File(selectedPath1));
                reqEntity.addPart("portfolio", filebodyimage);
            }

            reqEntity.addPart("vendorID", sb11);
            // reqEntity.addPart("remarks", sb1);
            //reqEntity.addPart("certificate_id", sb111);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

				/*if (!selectedPath1.equalsIgnoreCase("")) {*/

        try {

            if (cd.isConnectingToInternet()) {

                String url = getString(R.string.url_base_new)
                        + getString(R.string.updateVendorPortfolio);
                new AsyncPostDataFileResponseFragment(context, (ListenerPostData) this, 7, reqEntity, url);

            } else {

                AppUtils.showCustomAlert(
                        getActivity(),
                        getResources()
                                .getString(
                                        R.string.message_network_problem));
            }


        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }

    }


    private void deleteImage() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "imageID", imageID));

                new AsyncPostDataResponse(Fragment_VendorPortfolio.this, 5, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.deleteVendorPortfolio));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void setData() {

        if (b != null) {

            String data = b.getString("portfolio");

            try {
                JSONObject ob = new JSONObject(data);

                JSONArray images = ob.getJSONArray("images");

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    serviceDetail = new ModelService();
                    serviceDetail.setPorfolioImage(context.getResources().getString(
                            R.string.img_url) + jo.getString("image"));
                    serviceDetail.setImageId(jo.getString("imageID"));
                    imagelist.add(serviceDetail);

                }

                adapterPortfolio = new AdapterVendorPortfolio(context, this, imagelist);
                list_portfolio.setAdapter(adapterPortfolio);
                if (imagelist.size() == 2 || imagelist.size() > 2) {
                    text_approval.setVisibility(View.GONE);
                } else {
                    text_approval.setVisibility(View.VISIBLE);
                }
/*

                text_name.setText(ob.getString("name"));
                text_email.setText(ob.getString("email"));
                text_about.setText(ob.getString("about"));
                text_phone.setText(ob.getString("phone"));
*/


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
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
                    addPortfolio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                // circleImage.setImageBitmap(bitmap);
                break;
            case 513:
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                // circleImage.setImageBitmap(bitmap);
                break;
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {

            String images = "";

            for (int i = 0; i < imagelist.size(); i++) {

                if (images.length() > 0) {
                    images = images + "," + imagelist.get(i).getPorfolioImage();
                } else {
                    images = imagelist.get(i).getPorfolioImage();
                }

            }

            Log.e("position", "*" + position);
            Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
            intent.putExtra("posi", position);
            intent.putExtra("images", images);
            startActivity(intent);
           /* imageID = imagelist.get(position).getImageId();
            deletedimagePosition=position;
            showLogoutBox();*/
        }
        if (flag == 3) {

            imageID = imagelist.get(position).getImageId();
            deletedimagePosition = position;
            showLogoutBox();

        }

    }

    private void showLogoutBox() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());

        alertDialog.setTitle("Delete !");

        alertDialog.setMessage("Are you want to delete this portfolio image?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            deleteImage();
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }


    @Override
    public void onPostRequestSucess(int method, String response) {

        try {
            Log.e("onPostRequestSucess", "*" + method);
            if (method == 7) {

                Log.e("setadapter", "setadapter");
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    JSONArray images = data.getJSONArray("images");
                    for (int i = 0; i < images.length(); i++) {

                        JSONObject jo = images.getJSONObject(i);


                        serviceDetail = new ModelService();
                        serviceDetail.setPorfolioImage(context.getResources().getString(
                                R.string.img_url) + jo.getString("image"));
                        serviceDetail.setImageId(jo.getString("imageID"));
                        imagelist.add(serviceDetail);

                        Log.e("In forlop", imagelist.get(i).getPorfolioImage());

                    }
                   /* adapterPortfolio = new AdapterPortfolio(context, this, imagelist);
                    list_portfolio.setAdapter(adapterPortfolio);*/

                    adapterPortfolio.notifyDataSetChanged();
                    activity.updateData(2);

                    if (imagelist.size() == 2 || imagelist.size() > 2) {
                        text_approval.setVisibility(View.GONE);
                    } else {
                        text_approval.setVisibility(View.VISIBLE);
                    }

                }
            } else if (method == 5) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                    JSONObject data = commandResult.getJSONObject("data");
                    imagelist.remove(deletedimagePosition);
                    adapterPortfolio.notifyDataSetChanged();
                    activity.updateData(2);
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
    }
}

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.app.justclap.adapters.AdapterCertificates;
import com.app.justclap.asyntask.AsyncPostDataFileResponseFragment;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.customview.ExpandableHeightGridView;

import eu.janmuller.android.simplecropimage.CropImage;

import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.VendorEdit_BusinessDetail;
import com.app.justclap.vendordetail.Vendor_homePage;

public class Fragment_BusinessInfo extends Fragment implements OnCustomItemClicListener, ListenerPostData {


    RelativeLayout rl_edit_profile;
    TextView text_name, text_services, text_city, text_travel, text_location, text_website, text_facebbok, text_description, text_addcertificate;
    Context context;
    ExpandableHeightGridView list_certificates;
    ModelService serviceDetail;
    ArrayList<ModelService> imagelist;
    private Bitmap bitmap = null;
    Vendor_homePage activity;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    String path = "";
    private File mFileTemp;
    AdapterCertificates adapterCertificates;
    Bundle b;
    ConnectionDetector cd;
    String selectedPath1 = "";
    String imageID = "", serviceid = "0", cityId = "0";
    int deletedimagePosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewProfile = inflater.inflate(R.layout.fragment_business_info, container, false);

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

        rl_edit_profile = (RelativeLayout) viewProfile.findViewById(R.id.rl_edit_profile);
        b = getArguments();
        list_certificates = (ExpandableHeightGridView) viewProfile.findViewById(R.id.list_certificates);
        list_certificates.setExpanded(true);
        text_name = (TextView) viewProfile.findViewById(R.id.text_name);
        text_services = (TextView) viewProfile.findViewById(R.id.text_services);
        text_addcertificate = (TextView) viewProfile.findViewById(R.id.text_addcertificate);
        text_travel = (TextView) viewProfile.findViewById(R.id.text_travel);
        text_city = (TextView) viewProfile.findViewById(R.id.text_city);
        text_location = (TextView) viewProfile.findViewById(R.id.text_location);
        text_website = (TextView) viewProfile.findViewById(R.id.text_website);
        text_facebbok = (TextView) viewProfile.findViewById(R.id.text_facebbok);
        text_description = (TextView) viewProfile.findViewById(R.id.text_description);
        NestedScrollView scrollView = (NestedScrollView) viewProfile.findViewById(R.id.nest_scrollview1);
        scrollView.setFillViewport(true);
        setData();


        text_addcertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage1();
            }
        });

        rl_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("serviceidnew1", serviceid);
                Intent in = new Intent(getActivity(), VendorEdit_BusinessDetail.class);
                in.putExtra("name", text_name.getText().toString());
                in.putExtra("distance", text_travel.getText().toString().replace("Km", "").trim());
                in.putExtra("fb", text_facebbok.getText().toString());
                in.putExtra("web", text_website.getText().toString());
                in.putExtra("address", text_location.getText().toString());
                in.putExtra("service", serviceid);
                in.putExtra("servicename", text_services.getText().toString());
                //  in.putExtra("city", cityId);
                in.putExtra("desc", text_description.getText().toString());

                startActivityForResult(in, 52);

            }
        });

        return viewProfile;
    }

    public void setData() {

        if (b != null) {

            String data = b.getString("business");
            String certificate = b.getString("cert");


            try {
                JSONObject ob = new JSONObject(data);

                text_name.setText(ob.getString("businessName"));
                text_travel.setText(ob.getString("distance") + "Km");
                text_facebbok.setText(Html.fromHtml(ob.getString("fbUrl")));
                text_website.setText(Html.fromHtml(ob.getString("websiteUrl")));
                text_location.setText(ob.getString("address"));

                text_description.setText(ob.getString("businessDesc"));
                //   text_city.setText(ob.getString("cityName"));

                //  cityId = ob.getString("cityID");

                JSONArray services = ob.getJSONArray("services");
                JSONObject serv = services.getJSONObject(0);
                serviceid = serv.getString("serviceID");
                text_services.setText(serv.getString("serviceName"));

                JSONObject ob1 = new JSONObject(certificate);

                JSONArray images = ob1.getJSONArray("images");

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    serviceDetail = new ModelService();
                    serviceDetail.setPorfolioImage(context.getResources().getString(
                            R.string.img_url) + jo.getString("image"));
                    serviceDetail.setImageId(jo.getString("imageID"));
                    imagelist.add(serviceDetail);

                }

                adapterCertificates = new AdapterCertificates(context, this, imagelist);
                list_certificates.setAdapter(adapterCertificates);

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                reqEntity.addPart("certificate", filebodyimage);
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
                        + getString(R.string.updateVendorCertificate);
                new AsyncPostDataFileResponseFragment(context, (ListenerPostData) this, 3, reqEntity, url);

            } else {

                AppUtils.showCustomAlert(
                        getActivity(),
                        getResources()
                                .getString(
                                        R.string.message_network_problem));
            }


        } catch (Exception e)

        {
            Log.e("exception", e.getMessage());
        }

    }


    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Add Aggrement!");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 52 && resultCode == 513) {

            text_name.setText(data.getExtras().getString("businessName"));
            text_travel.setText(data.getExtras().getString("distance"));
            text_facebbok.setText(data.getExtras().getString("fbUrl"));
            text_website.setText(data.getExtras().getString("websiteUrl"));
            text_location.setText(data.getExtras().getString("address"));
            text_services.setText(data.getExtras().getString("service"));
            text_description.setText(data.getExtras().getString("businessDesc"));
            // text_city.setText(data.getExtras().getString("city"));
            serviceid = data.getExtras().getString("serviceid");
            // Log.e("serviceidnew", serviceid);
            //  cityId=data.getExtras().getString("cityid");
            activity.updateData(1);

        }

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
                // bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                // circleImage.setImageBitmap(bitmap);
                break;

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

                new AsyncPostDataResponse(Fragment_BusinessInfo.this, 6, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.deleteVendorPortfolio));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        /*    imageID = imagelist.get(position).getImageId();
            deletedimagePosition = position;
            showLogoutBox();*/


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
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 3) {
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

                    }

                    adapterCertificates.notifyDataSetChanged();

                } else {

                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                }
            } else if (position == 6) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                    JSONObject data = commandResult.getJSONObject("data");
                    imagelist.remove(deletedimagePosition);
                    adapterCertificates.notifyDataSetChanged();

                } else {

                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }
}

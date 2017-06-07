package com.app.anmolenterprise.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.aynctask.CommonAsyncTaskAquery;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.utils.AppUtils;
import com.app.anmolenterprise.utils.GpsTrackerAddress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import eu.janmuller.android.simplecropimage.CropImage;

public class FillForm extends AppCompatActivity implements ApiResponse, DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private EditText edt_name, edt_email, edt_address, edt_mobileno, edt_pincode, edt_comment, edt_alternativeno, edt_cmpnyname;
    private TextView edt_date, upload_image;
    private Toolbar toolbar;
    private Button btn_submit;
    private String lat = "0.0", lng = "0.0";
    private String formatted_address = "";
    private Location mLastLocation;
    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private double mLat = 0.0;
    private double mLong = 0.0;
    private GpsTrackerAddress gpsTrackerAddress;
    private String subCatname = "";
    String path = "", selectedPath1 = "";
    private File mFileTemp, selectedFilePath;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillform);
        context = this;
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        init();
        setListener();
        subCatname = getIntent().getStringExtra("name");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        googleapi();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Fill Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_pincode = (EditText) findViewById(R.id.edt_pincode);
        edt_alternativeno = (EditText) findViewById(R.id.edt_alternativeno);
        edt_cmpnyname = (EditText) findViewById(R.id.edt_cmpnyname);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_comment = (EditText) findViewById(R.id.edt_comment);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_mobileno = (EditText) findViewById(R.id.edt_mobileno);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_date = (TextView) findViewById(R.id.edt_date);
        upload_image = (TextView) findViewById(R.id.upload_image);
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidLoginDetails()) {
                    sendQuery();
                }
            }
        });
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(FillForm.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage1();
            }
        });
    }

    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Add Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePicture();

                } else if (items[item].equals("Choose from Gallery")) {

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

/*
    private void openFile() {

        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            String[] mimetypes = {"application/pdf"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            //    intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private void startCropImage() {

        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 2);
        intent.putExtra(CropImage.ASPECT_Y, 2);

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
                upload_image.setText("Image upload successfully");
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
                upload_image.setText("Image upload successfully");
                break;
        }

    }

    public void sendQuery() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

            /*    String params = "?mobile=" + edt_mobileno.getText().toString() + "&name=" + edt_name.getText().toString()
                        + "&address=" + edt_address.getText().toString()
                        + "&email=" + edt_email.getText().toString() + "&user_id=" + AppUtils.getUserId(context)
                        + "&pincode=" + edt_pincode.getText().toString() + "&alternative_no=" + edt_alternativeno.getText().toString()
                        + "&company_name=" + edt_cmpnyname.getText().toString() + "&product=" + subCatname + "&autolocation="
                        + "sdf"
                        + "&purchase_date=" + edt_date.getText().toString() + "&comment=" + edt_comment.getText().toString() +
                        "&image=" + selectedFilePath;*/

                HashMap<String, Object> hm = new HashMap<>();

                hm.put("mobile", edt_mobileno.getText().toString());
                hm.put("name", edt_name.getText().toString());
                hm.put("address", edt_address.getText().toString());
                hm.put("email", edt_email.getText().toString());
                hm.put("user_id", AppUtils.getUserId(context));
                hm.put("pincode", edt_pincode.getText().toString());
                hm.put("alternative_no", edt_alternativeno.getText().toString());
                hm.put("company_name", edt_cmpnyname.getText().toString());
                hm.put("product", subCatname);
                hm.put("autolocation", edt_address.getText().toString());
                hm.put("purchase_date", edt_date.getText().toString());
                hm.put("comment", edt_comment.getText().toString());
                hm.put("image", selectedFilePath);

                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.sendLead);

                new CommonAsyncTaskAquery(1, context, FillForm.this).getquery(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        if (!edt_mobileno.getText().toString().equalsIgnoreCase("") && !edt_cmpnyname.getText().toString().equalsIgnoreCase("")
                && !edt_pincode.getText().toString().equalsIgnoreCase("")
                && !edt_address.getText().toString().equalsIgnoreCase("") && !edt_name.getText().toString().equalsIgnoreCase("")) {


            isValidLoginDetails = true;
        } else {
            if (edt_name.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();

            } else if (edt_address.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();

            } else if (edt_pincode.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, "Please enter pincode", Toast.LENGTH_SHORT).show();

            } else if (edt_mobileno.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();

            } else if (edt_cmpnyname.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, "Please enter company name", Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, UserDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }

    private void googleapi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @SuppressLint("NewApi")
    private void connection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            } else {
                con();
            }
        } else
            con();
    }

    private void con() {
        Log.e("", "");
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            // startLocationUpdate();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.e("mLastLocation", "**" + mLastLocation);
            if (mLastLocation != null) {
                mLat = mLastLocation.getLatitude();
                mLong = mLastLocation.getLongitude();

                Log.e("latitude", "**" + mLat + "*" + mLong);

                address(mLat, mLong);
                if (String.valueOf(mLat) != null) {
                    //     startLocationUpdate();

                } else {
                    connection();

                }
            }
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        con();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connection();
                } else {
                    Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        connection();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void address(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address;
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                if (address.contains("Unnamed")) {
                    address = addresses.get(1).getAddressLine(0);
                }
                String city = addresses.get(0).getLocality();
                String sublocality = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                String newaddress = "";
                if (address == null)
                    newaddress = city + ", " + state;
                else if (city == null)
                    newaddress = address + ", " + state;
                else if (state == null)
                    newaddress = address + ", " + city;
                else if (address == null && state == null)
                    newaddress = city;
                else if (city == null && state == null)
                    newaddress = address;
                else
                    newaddress = knownName + ", " + sublocality + ", " + city;

                //newaddress = knownName + ", "+sublocality + ", " + city + ", " + state;
                String noSpaces = newaddress.replaceAll("\0", " ");
                formatted_address = noSpaces;
                edt_address.setText(formatted_address);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        String date = year + "-" + month + "-" + dayOfMonth;
        edt_date.setText(year + "-" + month + "-" + dayOfMonth);
    }
}

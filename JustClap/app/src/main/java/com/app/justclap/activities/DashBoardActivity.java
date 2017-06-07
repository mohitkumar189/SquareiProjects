package com.app.justclap.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapters.MyPagerAdapter;
import com.app.justclap.asyntask.AsyncPostDataFileResponse;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.fragment.FragmentDashboardServices;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

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

import eu.janmuller.android.simplecropimage.CropImage;

public class DashBoardActivity extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    FragmentManager fm;
    Context context;
    FrameLayout frameLayout;
    MyPagerAdapter pagerAdapter;
    private DrawerLayout drawerLayout;
    RelativeLayout rl_container;
    LinearLayout rl_header;
    FragmentDashboardServices fragmentDashboardServices;
    ConnectionDetector cd;
    ImageView image_user;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    String path = "", selectedPath1 = "";
    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    ViewPager pager;
    TextView text_userName, text_user_email;
    RelativeLayout rl_login, rl_home, rl_chat, rl_privacy, rl_works, rl_terms, rl_about,
            rl_contact, rl_booking, rl_rate, rl_share, rl_setting, rl_logout;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    public float lastSliderPosition = 0;
    private BroadcastReceiver broadcastReceiver;
    private TabLayout tabLayout;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dash_board);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        context = this;
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuline);
        fm = getSupportFragmentManager();
        Log.e("GcmPushKey", AppUtils.getGcmRegistrationKey(context));
        setListener();
        fragmentDashboardServices = new FragmentDashboardServices();
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 4);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;

        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }

        } else {
            SharedPreferences pref = getSharedPreferences("contact", MODE_PRIVATE);
            boolean isDeactivated = pref.getBoolean("Deactivate1", false);
            if (isDeactivated) {

            } else {
                Intent serviceIntent = new Intent(context, SubmitContactService.class);
                context.startService(serviceIntent);
            }
        }


        cd = new ConnectionDetector(context);
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
        rl_works = (RelativeLayout) findViewById(R.id.rl_works);
        rl_booking = (RelativeLayout) findViewById(R.id.rl_booking);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
        rl_contact = (RelativeLayout) findViewById(R.id.rl_contact);
        rl_rate = (RelativeLayout) findViewById(R.id.rl_rate);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
        rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        rl_privacy = (RelativeLayout) findViewById(R.id.rl_privacy);
        rl_terms = (RelativeLayout) findViewById(R.id.rl_terms);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById(R.id.header_layout);
        rl_header = (LinearLayout) findViewById(R.id.rl_header);
        rl_container = (RelativeLayout) findViewById(R.id.rl_container);
        text_user_email = (TextView) findViewById(R.id.text_useremail);
        text_userName = (TextView) findViewById(R.id.text_username);
        image_user = (ImageView) findViewById(R.id.image_user);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 1) {

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {


                    if (permissions[i].equals(Manifest.permission.READ_CONTACTS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            SharedPreferences pref = getSharedPreferences("contact", MODE_PRIVATE);
                            boolean isDeactivated = pref.getBoolean("Deactivate1", false);
                            if (isDeactivated) {


                            } else {

                                Intent serviceIntent = new Intent(context, SubmitContactService.class);
                                context.startService(serviceIntent);
                            }

                            Log.e("msg", "accounts granted");
                            // getContactList();
                        } else {


                        }
                    }
                }
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void setupTabIcons() {

        TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_withoutselector, null);
        tab.setText("Home");
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home1, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tab);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_withoutselector, null);
        tabTwo.setText("Requests");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.requests, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_withoutselector, null);
        tabThree.setText("Chats");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chats, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_withoutselector, null);
        tabfour.setText("Notifications");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notification_icon, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);

    }

    private void setListener() {

        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, AboutUs.class);
                startActivity(in);

            }
        });
        rl_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, BookingHistory.class);
                startActivity(in);

            }
        });
        rl_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, ActivityHome.class);
                in.putExtra("is_Vendor", "1");
                startActivity(in);

            }
        });
        rl_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, TermsAndconditions.class);
                startActivity(in);

            }
        });
        rl_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, PrivacyPolicy.class);
                startActivity(in);

            }
        });
        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //    drawerLayout.closeDrawer(frameLayout);
                if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
                    selectImage1();
                }

            }
        });

        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                pager.setCurrentItem(0);

            }
        });
        rl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, LoginHome.class);

                startActivityForResult(in, 511);

            }
        });
        rl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
              /*  Intent in = new Intent(DashBoardActivity.this, Login.class);
                startActivity(in);*/

            }
        });
        rl_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, ContactUs.class);
                startActivity(in);

            }
        });
        rl_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);

                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }

            }
        });

        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "JustClap ,a fastest growing App ,I find many new things here" +
                        ", You can also find, So why are you waiting Get it now." + "http://play.google.com/store/apps/details?id=" + context.getPackageName());
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(DashBoardActivity.this, ActivitySetting.class);
                startActivity(in);


            }
        });
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                showLogoutBox();

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar, //<-- This is the icon provided by Google itself
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {


            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?

                if (drawerLayout.isDrawerOpen(frameLayout)) {
                    drawerLayout.closeDrawer(frameLayout);

                } else {

                    drawerLayout.openDrawer(frameLayout);
                }
            }
        });
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //   Log.e("slide menu", "**" + slideOffset);

                ValueAnimator anim = ValueAnimator.ofFloat(lastSliderPosition, slideOffset);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //  Log.e("open", "draweropen");
                        float slideOffset = (Float) valueAnimator.getAnimatedValue();
                        mDrawerToggle.onDrawerSlide(drawerLayout, slideOffset);
                    }
                });
                anim.setInterpolator(new DecelerateInterpolator());
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                anim.setDuration(500);
                anim.start();
                mDrawerToggle.syncState();
                lastSliderPosition = slideOffset;


            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {


            }

            @Override
            public void onDrawerStateChanged(int newState) {

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
            StringBody sb11 = new StringBody(AppUtils.getUserId(context), encoding);
            //  StringBody sb111 = new StringBody(id, encoding);
            FileBody filebodyimage = null;
            selectedPath1 = path;
            if (!selectedPath1.equalsIgnoreCase("")) {
                filebodyimage = new FileBody(new File(selectedPath1));
                reqEntity.addPart("profile", filebodyimage);
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
                        + getString(R.string.updateVendorProfilePhoto);
                new AsyncPostDataFileResponse(context, 5, reqEntity, url);

            } else {

                AppUtils.showCustomAlert(
                        DashBoardActivity.this,
                        getResources()
                                .getString(
                                        R.string.message_network_problem));
            }


        } catch (Exception e)

        {
            Log.e("exception", e.getMessage());
        }


    }

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                DashBoardActivity.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                    2);
                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "userID", AppUtils.getUserId(context)));

                            new AsyncPostDataResponse(DashBoardActivity.this, 3, nameValuePairs,
                                    getString(R.string.url_base_new)
                                            + getString(R.string.logout));

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
    protected void onResume() {
        super.onResume();

        if (AppUtils.getUserId(context).equalsIgnoreCase("")) {

            rl_login.setVisibility(View.VISIBLE);
            rl_setting.setVisibility(View.GONE);
            rl_logout.setVisibility(View.GONE);
            rl_booking.setVisibility(View.GONE);
            rl_works.setVisibility(View.VISIBLE);
            text_user_email.setText("Welcome to JustClap");
            text_userName.setVisibility(View.GONE);

        } else {

            rl_login.setVisibility(View.GONE);
            rl_setting.setVisibility(View.VISIBLE);
            rl_booking.setVisibility(View.VISIBLE);
            rl_logout.setVisibility(View.VISIBLE);
            rl_works.setVisibility(View.GONE);
            text_user_email.setText(AppUtils.getUseremail(context));
            text_userName.setText(AppUtils.getUserName(context));
            text_userName.setVisibility(View.VISIBLE);
        }

        if (!AppUtils.getUserImage(context).equalsIgnoreCase("")) {

            Picasso.with(context)
                    .load(AppUtils.getUserImage(context))
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(image_user);

        } else {

            image_user.setImageResource(R.drawable.placeholder_logo);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 511 && resultCode == 512) {
            if (AppUtils.getUserId(context).equalsIgnoreCase("")) {

                rl_login.setVisibility(View.VISIBLE);
                rl_setting.setVisibility(View.GONE);
                rl_logout.setVisibility(View.GONE);
                rl_works.setVisibility(View.VISIBLE);
                text_user_email.setText("Welcome to just Clap");
                text_userName.setVisibility(View.GONE);

            } else {

                rl_login.setVisibility(View.GONE);
                rl_setting.setVisibility(View.VISIBLE);
                rl_logout.setVisibility(View.VISIBLE);
                rl_works.setVisibility(View.GONE);
                text_user_email.setText(AppUtils.getUseremail(context));
                text_userName.setText(AppUtils.getUserName(context));
                text_userName.setVisibility(View.VISIBLE);
            }
            if (!AppUtils.getUserImage(context).equalsIgnoreCase("")) {

                Picasso.with(context)
                        .load(AppUtils.getUserImage(context))
                        .placeholder(R.drawable.user)
                        .transform(new CircleTransform())
                        .into(image_user);

            } else {

                image_user.setImageResource(R.drawable.placeholder_logo);
            }

        }
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
                    bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                    addPortfolio();
                 /*   Picasso.with(context)
                            .load(f)
                            .transform(new CircleTransform())
                            .into(image_user);
                    Log.e("file", "**" + f.toString());
                    AppUtils.setUserImage(context, f.toString());*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {


    }


    @Override
    public void onPostRequestSucess(int position, String response) {

        try {
            if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    AppUtils.setUserId(context, "");
                    AppUtils.setUseremail(context, "");
                    AppUtils.setUserMobile(context, "");
                    AppUtils.setUserName(context, "");
                    AppUtils.setUserImage(context, "");
                    LoginManager.getInstance().logOut();
                    text_user_email.setText("Welcome to just Clap");
                    text_userName.setVisibility(View.GONE);
                    image_user.setImageResource(R.drawable.placeholder_logo);
                    rl_login.setVisibility(View.VISIBLE);
                    rl_setting.setVisibility(View.GONE);
                    rl_logout.setVisibility(View.GONE);
                    Intent in = new Intent(context, DashBoardActivity.class);
                    startActivity(in);
                    finish();

                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }

            } else if (position == 5) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    JSONArray images = data.getJSONArray("images");

                    JSONObject jo = images.getJSONObject(0);
                    AppUtils.showCustomAlert(DashBoardActivity.this, commandResult.getString("message"));
                    String img = getResources().getString(R.string.img_url) + jo.getString("image");
                    Picasso.with(context)
                            .load(img)
                            .placeholder(R.drawable.user)
                            .transform(new CircleTransform())
                            .into(image_user);
                    Log.e("file", "**" + img);
                    AppUtils.setUserImage(context, img);

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            alertDialogBuilder.setTitle("Are you sure you want to exit?");

            // set dialog message
            alertDialogBuilder

                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    //   AppUtils.setUserId(context,false);

                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                                    sendBroadcast(broadcastIntent);
                                    finish();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

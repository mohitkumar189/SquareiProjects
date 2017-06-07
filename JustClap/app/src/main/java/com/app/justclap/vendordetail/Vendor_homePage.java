package com.app.justclap.vendordetail;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.activities.AboutUs;
import com.app.justclap.activities.ActivityHome;
import com.app.justclap.activities.ActivityNotification;
import com.app.justclap.activities.ActivitySetting;
import com.app.justclap.activities.ContactUs;
import com.app.justclap.activities.DashBoardActivity;
import com.app.justclap.activities.InternalStorageContentProvider;
import com.app.justclap.activities.PrivacyPolicy;
import com.app.justclap.R;
import com.app.justclap.activities.TermsAndconditions;
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

import com.app.justclap.adapters.VendorProfilePagerAdapter;
import com.app.justclap.asyntask.AsyncPostDataFileResponse;
import com.app.justclap.asyntask.AsyncPostDataResponse;

import eu.janmuller.android.simplecropimage.CropImage;

import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;

/**
 * Created by admin on 12-02-2016.
 */
public class Vendor_homePage extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {

    Context context;
    FrameLayout frameLayout;
    RelativeLayout rl_credits, rl_profile, rl_about,
            rl_works, rl_privacy, rl_terms, rl_login, rl_home, rl_chat, rl_contact, rl_rate, rl_share, rl_setting, rl_logout;
    TextView text_vendor_name;
    ImageView image_menu, image_background;
    VendorProfilePagerAdapter vendorProfilePagerAdapter;
    TextView text_userName, text_user_email;
    ImageView image_user;
    DrawerLayout drawerLayout;
    ImageView image_profile, image_edit;
    ViewPager pager;
    RatingBar rating;
    int setPagerPosition = 0;
    ModelService detaildata;
    ArrayList<ModelService> arrayList;
    ConnectionDetector cd;
    int typePhoto;
    private Bitmap bitmap = null;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    String path = "";
    private ImageView mImageView;
    private File mFileTemp;
    RelativeLayout rl_main_layout, rl_network;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    String selectedPath1 = "";
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.idicon,
            R.drawable.bsnsicon,
            R.drawable.photoicon
    };
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    public float lastSliderPosition = 0;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home_page);
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuline);
        setListener();
        Intent in = getIntent();

        if (in.hasExtra("pagerPosition")) {
            setPagerPosition = in.getExtras().getInt("pagerPosition");
        }
        arrayList = new ArrayList<>();
        setupCollapsingToolbar();
        text_vendor_name.setText(AppUtils.getvendorname(context));
        getVendorData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
       /* NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport(true);*/

    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Profile");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.idicon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Business");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bsnsicon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Portfolio");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.photoicon, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorPrimary),
                getResources().getColor(R.color.txt_orange));

    }

    private void init() {
        overridePendingTransition(0, 0);
     overridePendingTransition(R.anim.enter,
                R.anim.exit);

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
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        rating = (RatingBar) findViewById(R.id.rating);
        rating.setEnabled(false);
        text_vendor_name = (TextView) findViewById(R.id.text_vendor_name);
        image_background = (ImageView) findViewById(R.id.image_background);
        pager = (ViewPager) findViewById(R.id.pager);
        image_menu = (ImageView) findViewById(R.id.image_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById(R.id.header_layout);
        image_profile = (ImageView) findViewById(R.id.image_vendor);
        image_user = (ImageView) findViewById(R.id.image_user);
        text_user_email = (TextView) findViewById(R.id.text_useremail);
        text_userName = (TextView) findViewById(R.id.text_username);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
        rl_contact = (RelativeLayout) findViewById(R.id.rl_contact);
        rl_rate = (RelativeLayout) findViewById(R.id.rl_rate);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
        rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
        rl_credits = (RelativeLayout) findViewById(R.id.rl_credits);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        rl_privacy = (RelativeLayout) findViewById(R.id.rl_privacy);
        rl_terms = (RelativeLayout) findViewById(R.id.rl_terms);
        image_edit = (ImageView) findViewById(R.id.image_edit);
    }


    private void getVendorData() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                //  AppUtils.showCustomAlert(Vendor_homePage.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));

                new AsyncPostDataResponse(Vendor_homePage.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void updateData(int pos) {

      /*  Intent in = new Intent(context, Vendor_homePage.class);
        in.putExtra("pagerPosition", pos);
        startActivity(in);
        finish();
        */
        getVendorData();
        setPagerPosition = pos;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }

    private void setListener() {

        rl_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, TermsAndconditions.class);
                startActivity(in);

            }
        });
        rl_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, PrivacyPolicy.class);
                startActivity(in);

            }
        });

        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, AboutUs.class);
                startActivity(in);

            }
        });


        rl_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, ActivityHome.class);
                in.putExtra("is_Vendor", "2");
                startActivity(in);

            }
        });
        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

                    Intent intent = new Intent(Vendor_homePage.this, Vendor_Lead_dashboardBidirectional.class);
                    startActivity(intent);
                    finish();

                } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {
                    Intent intent = new Intent(Vendor_homePage.this, Vendor_Lead_dashboard.class);
                    startActivity(intent);
                    finish();
                }


            }
        });
        rl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, VendorLogin.class);
                startActivityForResult(in, 511);

            }
        });
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, Vendor_homePage.class);
                startActivity(in);
                finish();

            }
        });
        rl_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_homePage.this, Vendor_Wallet.class);
                startActivity(in);


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
                Intent in = new Intent(Vendor_homePage.this, ContactUs.class);
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
                Intent in = new Intent(Vendor_homePage.this, ActivitySetting.class);
                in.putExtra("is_vendor", "1");
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
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typePhoto = 1;
                selectImageprofile();
            }
        });

        image_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*    typePhoto = 2;
                selectImageCover();*/
            }
        });

        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typePhoto = 2;
                selectImageCover();
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
                // supportInvalidateOptionsMenu();
                // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

            }

            @Override
            public void onDrawerClosed(View drawerView) {


                // supportInvalidateOptionsMenu();


            }

            @Override
            public void onDrawerStateChanged(int newState) {

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

    private void selectImageCover() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Add Cover Photo!");
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

    private void showLogoutBox() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Vendor_homePage.this);

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
                                            "userID", AppUtils.getvendorId(context)));

                            new AsyncPostDataResponse(Vendor_homePage.this, 3, nameValuePairs,
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

        if (AppUtils.getvendorId(context).equalsIgnoreCase("")) {

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
            text_user_email.setText(AppUtils.getvendorEmail(context));
            text_userName.setText(AppUtils.getvendorname(context));
            text_userName.setVisibility(View.VISIBLE);
        }
        if (!AppUtils.getVendorImage(context).equalsIgnoreCase("")) {

            Picasso.with(context)
                    .load(AppUtils.getVendorImage(context))
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(image_user);

            Picasso.with(context)
                    .load(AppUtils.getVendorImage(context))
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(image_profile);

        } else {

            image_user.setImageResource(R.drawable.placeholder_logo);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode", requestCode + "");
        if (requestCode == 511 && resultCode == 512) {
            if (AppUtils.getvendorId(context).equalsIgnoreCase("")) {

                rl_login.setVisibility(View.VISIBLE);
                rl_setting.setVisibility(View.GONE);
                rl_works.setVisibility(View.VISIBLE);
                rl_logout.setVisibility(View.GONE);
                text_user_email.setText("Welcome to just Clap");
                text_userName.setVisibility(View.GONE);

            } else {

                rl_login.setVisibility(View.GONE);
                rl_setting.setVisibility(View.VISIBLE);
                rl_logout.setVisibility(View.VISIBLE);
                rl_works.setVisibility(View.GONE);
                text_user_email.setText(AppUtils.getvendorEmail(context));
                text_userName.setText(AppUtils.getvendorname(context));
                text_userName.setVisibility(View.VISIBLE);
            }
            if (!AppUtils.getVendorImage(context).equalsIgnoreCase("")) {

                Picasso.with(context)
                        .load(AppUtils.getVendorImage(context))
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

                    bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                    addPortfolio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // image_profile.setImageBitmap(bitmap);
                break;

        }

    }

    private void addPortfolio() {


        if (typePhoto == 2) {

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
                    reqEntity.addPart("cover", filebodyimage);
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
                            + getString(R.string.updateVendorCoverPhoto);
                    new AsyncPostDataFileResponse(context, 4, reqEntity, url);

                } else {

                    AppUtils.showCustomAlert(
                            Vendor_homePage.this,
                            getResources()
                                    .getString(
                                            R.string.message_network_problem));
                }


            } catch (Exception e)

            {
                Log.e("exception", e.getMessage());
            }
        } else if (typePhoto == 1) {

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
                            Vendor_homePage.this,
                            getResources()
                                    .getString(
                                            R.string.message_network_problem));
                }


            } catch (Exception e)

            {
                Log.e("exception", e.getMessage());
            }
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

                    AppUtils.setvendorId(context, "");
                    AppUtils.setvendorEmail(context, "");
                    AppUtils.setvendormobile(context, "");
                    AppUtils.setvendorname(context, "");
                    AppUtils.setvendoruserId(context, "");
                    AppUtils.setVendorImage(context, "");
                    Intent in = new Intent(Vendor_homePage.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();

                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }
            } else if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                arrayList = new ArrayList<>();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");
                    if (!data.getString("coverImage").equalsIgnoreCase("")) {

                        Picasso.with(context).load(getResources().getString(R.string.img_url) + data.getString("coverImage"))
                                // .placeholder(R.drawable.placholder_profile)
                                //.skipMemoryCache()
                                // .transform(new CircleTransform())
                                .into(image_background);
                    }

                    rating.setRating(Float.parseFloat(data.getString("avgRating")));

                    detaildata = new ModelService();
                    detaildata.setPersonalDetailsArray(data.getJSONObject("personalDetails").toString());
                    detaildata.setBussinessDetailsArray(data.getJSONObject("bussinessDetails").toString());
                    detaildata.setPortfolioDetailsArray(data.getJSONObject("portfolioDetails").toString());
                    detaildata.setCertificateDetailsArray(data.getJSONObject("certificateDetails").toString());

                    arrayList.add(detaildata);

                    Log.e("arraylistasize", "**" + arrayList.size());

                    vendorProfilePagerAdapter = new VendorProfilePagerAdapter(getSupportFragmentManager(), 3, arrayList);
                    pager.setAdapter(vendorProfilePagerAdapter);
                    tabLayout.setupWithViewPager(pager);
                    setupTabIcons();
                    pager.setCurrentItem(setPagerPosition);

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);


                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                    finish();
                }

            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                arrayList = new ArrayList<>();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.showCustomAlert(Vendor_homePage.this, commandResult.getString("message"));
                    JSONArray images = data.getJSONArray("images");

                    JSONObject jo = images.getJSONObject(0);

                    String img = getResources().getString(R.string.img_url) + jo.getString("image");
                    Picasso.with(context)
                            .load(img)
                            //  .placeholder(R.drawable.placeholder_logo)
                            //    .transform(new CircleTransform())
                            .into(image_background);
                    // image_background.setImageBitmap(bitmap);

                }


            } else if (position == 5) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                arrayList = new ArrayList<>();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.showCustomAlert(Vendor_homePage.this, commandResult.getString("message"));
                    JSONArray images = data.getJSONArray("images");

                    JSONObject jo = images.getJSONObject(0);

                    String img = getResources().getString(R.string.img_url) + jo.getString("image");
                    Picasso.with(context)
                            .load(img)
                            .placeholder(R.drawable.user)
                            .transform(new CircleTransform())
                            .into(image_profile);
                    Log.e("file", "**" + img);
                    AppUtils.setVendorImage(context, img);


                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(Vendor_homePage.this, getResources().getString(R.string.problem_server));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.wallet_icon) {

            Intent intent = new Intent(Vendor_homePage.this, Vendor_Wallet.class);
            startActivity(intent);
        }
        if (id == R.id.notification_icon) {

            Intent intent = new Intent(Vendor_homePage.this, ActivityNotification.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}

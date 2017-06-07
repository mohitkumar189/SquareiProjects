package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class GalleryViewActivity extends AppCompatActivity {

    int posi = 0;

    private ScreenSlidePagerAdapter mPagerAdapter;
    ViewPager mPager;
    private CirclePageIndicator mIndicator;
    Toolbar toolbar;
    private String image1 = "";
    ArrayList<String> imageArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleryview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery View");
        Intent in = getIntent();
        posi = in.getExtras().getInt("posi");

        image1 = in.getExtras().getString("images");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();
            }

        });

        initViews();

        String images[] = image1.split(",");

        for (int i = 0; i < images.length; i++) {
            imageArray.add(images[i]);

        }


        mPagerAdapter = new ScreenSlidePagerAdapter(GalleryViewActivity.this, imageArray);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(posi);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



            }

            @Override
            public void onPageSelected(int position) {



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        mPager = (ViewPager) findViewById(R.id.pager);

    }

    class ScreenSlidePagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> imageArray;

        public ScreenSlidePagerAdapter(Context context, ArrayList<String> imageArray) {
            mContext = context;
            this.imageArray = imageArray;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imageArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item_gallery_view, container, false);
            ImageView imagView = (ImageView) itemView.findViewById(R.id.imageView);

            Log.e("image_positionAdapter",imageArray.get(position));
            Picasso.with(mContext).load(imageArray.get(position).trim())
                    .error(R.drawable.placeholder)
                    .into(imagView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

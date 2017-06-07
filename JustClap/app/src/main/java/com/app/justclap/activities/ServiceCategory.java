package com.app.justclap.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.justclap.R;

import com.app.justclap.fragment_types.Fragment_CheckboxTypeQues;

public class ServiceCategory extends AppCompatActivity {


    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment_CheckboxTypeQues fragment_selectCategory;
    Context context;
    ImageView imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_service_category);

        context = this;
        init();
        setListener();

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        fragment_selectCategory = new Fragment_CheckboxTypeQues();
        fragmentTransaction.replace(R.id.rl_container, fragment_selectCategory);
        fragmentTransaction.commit();


    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);


        imageView2 = (ImageView) findViewById(R.id.imageView2);

    }

    private void setListener() {


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }
}

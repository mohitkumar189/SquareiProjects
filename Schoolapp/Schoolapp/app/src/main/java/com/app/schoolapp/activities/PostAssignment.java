package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.adapter.AdapterPhotoList;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.utils.AppUtils;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class PostAssignment extends AppCompatActivity implements OnCustomItemClicListener, DatePickerDialog.OnDateSetListener, ApiResponse {


    private Context context;
    private Toolbar toolbar;
    private TextView text_class, text_section, text_submissiondate, text_subject;
    private Spinner spinner_class, spinner_section, spinner_subject;
    private EditText edt_title, edt_desc;
    private Button btn_submit;
    private ArrayList<String> listClass;
    private ArrayList<String> listSection;
    private ArrayList<String> listSubject;
    private RecyclerView recycler_list;
    private RelativeLayout rl_add_image, rl_submissiondate;
    ArrayList<String> imagesPath = new ArrayList<>();
    private String selectedimagespath = "";
    private AdapterPhotoList adapterPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_assignment);
        context = this;
        init();
        setListener();
        setData();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_class = (TextView) findViewById(R.id.text_class);
        text_section = (TextView) findViewById(R.id.text_section);
        text_submissiondate = (TextView) findViewById(R.id.text_submissiondate);
        text_subject = (TextView) findViewById(R.id.text_subject);
        spinner_class = (Spinner) findViewById(R.id.spinner_class);
        spinner_section = (Spinner) findViewById(R.id.spinner_section);
        spinner_subject = (Spinner) findViewById(R.id.spinner_subject);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        rl_add_image = (RelativeLayout) findViewById(R.id.rl_add_image);
        rl_submissiondate = (RelativeLayout) findViewById(R.id.rl_submissiondate);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new GridLayoutManager(context, 3));
    }

    private void setData() {
        listClass = new ArrayList<>();
        listSection = new ArrayList<>();
        listSubject = new ArrayList<>();

        listClass.add("1st");
        listClass.add("2nd");
        listClass.add("3rd");
        listClass.add("4th");
        listClass.add("5th");
        listClass.add("6th");
        listClass.add("7th");
        listClass.add("8th");
        listClass.add("9th");
        listClass.add("10th");

        ArrayAdapter<String> adpClass = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_time, listClass);
        spinner_class.setAdapter(adpClass);

        listSection.add("A");
        listSection.add("B");
        listSection.add("C");
        listSection.add("D");

        ArrayAdapter<String> adpSection = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_time, listSection);
        spinner_section.setAdapter(adpSection);

        listSubject.add("Hindi");
        listSubject.add("English");
        listSubject.add("Maths");
        listSubject.add("Science");
        listSubject.add("Art");
        ArrayAdapter<String> adpSubject = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_time, listSubject);
        spinner_subject.setAdapter(adpSubject);

    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_submissiondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(PostAssignment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "startdate");
            }
        });

        rl_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
        text_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_class.performClick();
            }
        });
        text_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_section.performClick();
            }
        });

        text_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_subject.performClick();
            }
        });
        spinner_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text_section.setText(spinner_section.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text_class.setText(spinner_class.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text_subject.setText(spinner_subject.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void submitData() {

        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, String> hm = new HashMap<>();

          /*  hm.put("class",spinner_class.getSelectedItem().toString());
            hm.put("class",spinner_class.getSelectedItem().toString());
            hm.put("class",spinner_class.getSelectedItem().toString());*/
            hm.put("client_id", AppUtils.getUserId(context));
            hm.put("title", edt_title.getText().toString());
            hm.put("content", edt_desc.getText().toString());
            hm.put("assignedclass", text_class.getText().toString().trim());
            hm.put("assignedsection", text_section.getText().toString().trim());
            hm.put("subject", text_subject.getText().toString().trim());
            hm.put("submissiondate", text_class.getText().toString().trim());

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.assignment_create);
            new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images

            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            selectedimagespath = "";
            imagesPath = new ArrayList<>();
            for (int i = 0, l = images.size(); i < l; i++) {
                imagesPath.add(images.get(i).path);
                stringBuffer.append(images.get(i).path + ",");
            }

            selectedimagespath = Arrays.deepToString(imagesPath.toArray());
            Log.e("selectedImagesPath", "*" + selectedimagespath);
            ArrayList<File> imagesFilelist = new ArrayList<>();
            if (!selectedimagespath.equalsIgnoreCase("")) {

                for (int i = 0; i < imagesPath.size(); i++) {

                    int j = i + 1;
                    File file = new File(imagesPath.get(i));
                    imagesFilelist.add(file);

                }
            }
            adapterPhoto = new AdapterPhotoList(context, this, imagesFilelist);
            recycler_list.setAdapter(adapterPhoto);

            //  text_imagecount.setText(imagesPath.size() + " Images uploaded");
            //  text_attach_images.setText("Images attached succesfully");

        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        text_submissiondate.setText(year + "-" + month + "-" + dayOfMonth);

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            if (success == 1 && error == 0) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}

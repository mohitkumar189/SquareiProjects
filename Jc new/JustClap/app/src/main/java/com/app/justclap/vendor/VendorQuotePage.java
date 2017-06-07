package com.app.justclap.vendor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelVendor;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendoradapter.AdapterAnswerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VendorQuotePage extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private Context context;
    private Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    private TextView text_servicename;
    private RecyclerView recycler_view;
    private RelativeLayout rl_quote;
    private EditText edt_price;
    private Button btn_submit;
    private ImageView image_delete, image_chat, image_call;
    private TextView text_name, text_email, text_requestcode, text_date, text_name_title, text_distance;
    private ImageView circle_image;
    AdapterAnswerList adapterAnswerList;
    ModelVendor ansList;
    ArrayList<ModelVendor> questionanswer;
    public ArrayList<Object> pagerDataList;
    private String vendorarray = "";
    int[] color = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r, R.drawable.s, R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x, R.drawable.y, R.drawable.z};
    ModelVendor modelService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_quote_page);
        context = this;
        init();
        setListener();

        setData();
    }

    private void setData() {
        try {
            vendorarray = getIntent().getStringExtra("vendorarray");

            JSONObject jo = new JSONObject(vendorarray);
            modelService = new ModelVendor();

            modelService.setRequestId(jo.getString("RequestId"));
            modelService.setServiceId(jo.getString("ServiceId"));
            modelService.setIsUnidirectional(jo.getString("IsUnidirectional"));
            modelService.setRequestCode(jo.getString("RequestCode"));
            modelService.setRequestLatitude(jo.getString("RequestLatitude"));
            modelService.setRequestLongitude(jo.getString("RequestLongitude"));
            modelService.setRequestDate(jo.getString("RequestDate"));
            modelService.setRequestTime(jo.getString("RequestTime"));
            modelService.setServiceName(jo.getString("ServiceName"));
            modelService.setServiceIcon(jo.getString("ServiceIcon"));
            modelService.setRequestQueriesArray(jo.getString("RequestQueries"));
            modelService.setRowType(1);
            modelService.setLeadId(jo.getString("LeadId"));
            modelService.setIsReviewed(jo.getString("IsReviewed"));
            modelService.setRewardPoint(jo.getString("RewardPoint"));
            modelService.setOfferPercentage(jo.getString("OfferPercentage"));
            modelService.setUserId(jo.getString("UserId"));
            modelService.setUserName(jo.getString("UserName"));
            modelService.setUserEmail(jo.getString("UserEmail"));
            modelService.setUserMobile(jo.getString("UserMobile"));
            modelService.setUserImage(jo.getString("UserImage"));
            modelService.setUserCoverImage(jo.getString("UserCoverImage"));

            text_name.setText(modelService.getUserName());
            text_date.setText(modelService.getRequestDate() + "  " + modelService.getRequestTime());
            text_requestcode.setText(modelService.getRequestCode());
            text_servicename.setText(modelService.getServiceName());

            try {
                if (modelService.getUserName().length() > 0) {
                    char c = modelService.getUserName().toUpperCase().charAt(0);
                    int pos = (int) c;
                    pos = pos % 65;
                    circle_image.setImageResource(color[pos]);
                    text_name_title.setText(modelService.getUserName().toUpperCase().charAt(0) + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setCustomerData(modelService.getRequestQueriesArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCustomerData(String customerResponse) {
        try {
            String inf = customerResponse;

            String ret = inf.replace("\\", "");
            JSONArray questionArray1 = new JSONArray(ret);
            Log.e("questionArray1", questionArray1.toString());

            for (int i = 0; i < questionArray1.length(); i++) {

                JSONObject jo = questionArray1.getJSONObject(i);
                pagerDataList.add(createQuestionModerData(jo));
            }
            adapterAnswerList = new AdapterAnswerList(context, this, questionanswer);
            recycler_view.setAdapter(adapterAnswerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ModelVendor> createQuestionModerData(JSONObject jsonData) {
        String date = "";
        try {
            int type = Integer.parseInt(jsonData.getString("questionTypeId"));

            switch (type) {

                case 1:
                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject jo1 = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(jo1.getString("optionText"));
                    questionanswer.add(ansList);
                    break;

                case 2:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject jo2 = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(jo2.getString("optionText"));
                    questionanswer.add(ansList);

                    break;

                case 3:
                    try {
                        ansList = new ModelVendor();
                        ansList.setQuestion(jsonData.getString("questionText"));
                        JSONObject jloc4 = jsonData.getJSONObject("questionOptions");

                        ansList.setAnswer(jloc4.getString("optionText"));
                        questionanswer.add(ansList);
                        if (date.length() > 0) {
                            date = date + ", " + jloc4.getString("optionText");
                        } else {
                            date = jloc4.getString("optionText");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));
                    JSONObject jloc = jsonData.getJSONObject("questionOptions");

                    String lat = jloc.getString("latitude");
                    String lon = jloc.getString("longitude");
                    ansList.setAnswer(jloc.getString("optionText"));
                    questionanswer.add(ansList);
                    break;

                case 5:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONArray jo4 = jsonData.getJSONArray("questionOptions");
                    String s = "";
                    for (int k = 0; k < jo4.length(); k++) {

                        JSONObject j5 = jo4.getJSONObject(k);
                        if (s.length() > 0) {
                            s = s + ", " + j5.getString("optionText");
                        } else {
                            s = j5.getString("optionText");
                        }
                    }
                    ansList.setAnswer(s);
                    questionanswer.add(ansList);

                    break;

                case 6:
                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));
                    JSONObject j3 = jsonData.getJSONObject("questionOptions");
                    if (date.length() > 0) {
                        date = date + ", " + j3.getString("optionText");
                    } else {
                        date = j3.getString("optionText");
                    }
                    ansList.setAnswer(j3.getString("optionText"));
                    questionanswer.add(ansList);
                    break;

                case 7:
                    ansList = new ModelVendor();
                    JSONObject jloc1 = jsonData.getJSONObject("questionOptions");
                    ansList.setQuestion(jsonData.getString("questionText"));
                    String lat1 = jloc1.getString("latitude");
                    String lon1 = jloc1.getString("longitude");
                    ansList.setAnswer(jloc1.getString("optionText"));
                    questionanswer.add(ansList);
                    break;

                case 8:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject j2 = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(j2.getString("optionText"));
                    questionanswer.add(ansList);
                    break;

                case 9:
                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject options = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(options.getString("optionText"));
                    questionanswer.add(ansList);

                    break;
                case 10:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject option = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(option.getString("optionText"));
                    questionanswer.add(ansList);

                    break;

                case 11:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject option1 = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(option1.getString("optionText"));
                    questionanswer.add(ansList);

                    break;
                case 12:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject option2 = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer(option2.getString("optionText"));
                    questionanswer.add(ansList);

                    break;

                case 14:

                    ansList = new ModelVendor();
                    ansList.setQuestion(jsonData.getString("questionText"));

                    JSONObject option21 = jsonData.getJSONObject("questionOptions");
                    ansList.setAnswer("From : " + option21.getString("src_address") + "\nTo : " + option21.getString("dest_address") +
                            "\nDate & Time : " + option21.getString("selected_date") + ", " + option21.getString("selected_time"));
                    questionanswer.add(ansList);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questionanswer;

    }

    private void init() {
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
        questionanswer = new ArrayList<>();
        pagerDataList = new ArrayList<>();
        text_servicename = (TextView) findViewById(R.id.text_servicename);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rl_quote = (RelativeLayout) findViewById(R.id.rl_quote);
        edt_price = (EditText) findViewById(R.id.edt_price);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Lead");

        image_call = (ImageView) findViewById(R.id.image_call);
        image_chat = (ImageView) findViewById(R.id.image_chat);
        image_delete = (ImageView) findViewById(R.id.image_delete);

        text_name = (TextView) findViewById(R.id.text_name);
        text_name_title = (TextView) findViewById(R.id.text_name_title);
        text_date = (TextView) findViewById(R.id.text_date);
        text_distance = (TextView) findViewById(R.id.text_distance);
        text_requestcode = (TextView) findViewById(R.id.text_requestcode);
        text_email = (TextView) findViewById(R.id.text_email);
        circle_image = (ImageView) findViewById(R.id.circle_image);

        AppUtils.fontGotham_Medium(text_name, context);
        AppUtils.fontGotham_Book(text_date, context);
        AppUtils.fontGotham_Book(text_distance, context);
        AppUtils.fontGotham_Book(text_name_title, context);
        AppUtils.fontGotham_Book(text_email, context);
        AppUtils.fontGotham_Book(text_requestcode, context);


    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_price.getText().toString().equalsIgnoreCase("")) {
                    submitQuote();
                } else {
                    Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*
      * for getting all requests detail
      */
    private void submitQuote() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();


                hm.put("userId", "1122");
                hm.put("quoteValue", edt_price.getText().toString());
                hm.put("leadId", modelService.getLeadId());

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.quoteToLead);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    //  JSONObject maindata = commandResult.getJSONObject("data");
                    setResult(22);
                    finish();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}

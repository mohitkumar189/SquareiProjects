package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.parent.ParentDashboard;
import com.app.schoolapp.service.GPSTracker;
import com.app.schoolapp.student.StudentDashboard;
import com.app.schoolapp.teacher.TeacherDashboard;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity implements ApiResponse {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = this;
        startService(new Intent(context, GPSTracker.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    String userType = AppUtils.getUserType(context);
                    switch (userType) {
                        case "Student":
                            Intent intent = new Intent(context, StudentDashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case "Parent":
                            Intent intent2 = new Intent(context, ParentDashboard.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent2);
                            finish();
                            break;
                        case "Teacher":
                            Intent intent3 = new Intent(context, TeacherDashboard.class);
                            intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent3);
                            finish();
                            break;
                        default:
                            Intent intent4 = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent4);
                            finish();
                            break;
                    }
             /*      if(AppUtils.getUserType(context).equalsIgnoreCase("")){

                   }else {
                       Intent intent = new Intent(getApplicationContext(), ParentDashboard.class);
                       startActivity(intent);
                       finish();
                   }*/

                }
            }
        }, 1000);
        //getData();

    }

    private void getData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

             /*   HashMap<String, Object> hm = new HashMap<>();*/
                String url = "";
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            if (method == 1) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }

}

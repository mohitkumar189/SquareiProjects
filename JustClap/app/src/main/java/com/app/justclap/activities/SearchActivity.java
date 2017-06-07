package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterSearchList;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

public class SearchActivity extends AppCompatActivity implements OnCustomItemClicListener, SearchView.OnQueryTextListener {


    Context context;
    AdapterSearchList adapterSearchList;
    private BroadcastReceiver broadcastReceiver;
    private RecyclerView mRecyclerView;
    ArrayList<ModelService> service_list;
    ModelService serviceDetail;
    Toolbar toolbar;
    RelativeLayout rl_main_layout, rl_network;
    String servicesArray = "";
    boolean isBack = false;
    SearchView searchView;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // search_list.setVisibility(View.INVISIBLE);
        service_list = new ArrayList<>();

        setListener();
        parseCategoryData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void init() {
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
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private void parseCategoryData() {

        servicesArray = AppUtils.getcategories(context);

        try {

            JSONArray categories = new JSONArray(servicesArray);

            for (int i = 0; i < categories.length(); i++) {

                JSONObject jo = categories.getJSONObject(i);
                serviceDetail = new ModelService();
                //  Log.e("categories", jo.getString("serviceName"));
                serviceDetail.setServiceID(jo.getString("categoryID"));
                serviceDetail.setServiceName(jo.getString("categoryName"));
                serviceDetail.setServiceIcon(jo.getString("categoryBGImage"));
                serviceDetail.setIsService("0");
                serviceDetail.setServicesArray(jo.getString("services"));
                serviceDetail.setIs_uniDirectional(jo.getString("is_uniDirectional"));
                service_list.add(serviceDetail);

            }

            JSONObject jo1 = categories.getJSONObject(0);
            JSONArray services = jo1.getJSONArray("services");

            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                serviceDetail = new ModelService();
                //   Log.e("categories", jo.getString("serviceName"));

                serviceDetail.setServiceID(jo.getString("serviceID"));
                serviceDetail.setServiceName(jo.getString("serviceName"));
                serviceDetail.setServiceIcon(jo.getString("serviceIcon"));
                serviceDetail.setIsService("1");
                serviceDetail.setServicesArray("");
                serviceDetail.setIs_uniDirectional("1");
                service_list.add(serviceDetail);

            }
            adapterSearchList = new AdapterSearchList(context, this, service_list);
            mRecyclerView.setAdapter(adapterSearchList);

            mRecyclerView.setVisibility(View.GONE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setListener() {


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
            }

        });

    }

    @Override
    public void onItemClickListener(int position, int flag) {


        if (flag == 1) {

            AppUtils.onKeyBoardDown(context);

            ModelService model = adapterSearchList.getFilter(position);

            if (model.getIs_uniDirectional().equalsIgnoreCase("2")) {

                //  Log.e("serviceid", model.getServiceID());
                Intent in = new Intent(SearchActivity.this, QuestionPage.class);
                in.putExtra("servicename", model.getServiceName());
                in.putExtra("serviceid", model.getServiceID());
                in.putExtra("Is_uniDirectional", model.getIs_uniDirectional());
                startActivityForResult(in, 22);

            } else {

                if (model.getIsService().equalsIgnoreCase("0")) {

                    Intent in = new Intent(SearchActivity.this, ServiceSubMenu.class);
                    in.putExtra("categoryName", model.getServiceName());
                    in.putExtra("servicearray", model.getServicesArray());
                    in.putExtra("Is_uniDirectional", model.getIs_uniDirectional());
                    startActivity(in);
                } else {
                    Intent in = new Intent(SearchActivity.this, QuestionPage.class);
                    in.putExtra("servicename", model.getServiceName());
                    in.putExtra("serviceid", model.getServiceID());
                    startActivity(in);
                }

            }

        }

    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 22 && resultCode == 23) {
            Log.e("onActivityResult", "back");
            isBack = true;

        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       /* if (isBack) {
            if (!this.searchView.isIconified()) {
                this.searchView.setIconified(true);
            } else {
                super.onBackPressed();

            }*/
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);


    }

    /* @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {

         if (keyCode == KeyEvent.KEYCODE_BACK) {


         }
         return super.onKeyDown(keyCode, event);
     }
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service, menu);

        item = menu.findItem(R.id.searchview);
        item.expandActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(SearchActivity.this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        //  adapterSearchList.setFilter(service_list);
                        finish();

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.searchview) {


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        final ArrayList<ModelService> filteredModelList = filter(service_list, query);
        adapterSearchList.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        mRecyclerView.setVisibility(View.VISIBLE);
        final ArrayList<ModelService> filteredModelList = filter(service_list, newText);
        adapterSearchList.setFilter(filteredModelList);
        return true;
    }

    private ArrayList<ModelService> filter(ArrayList<ModelService> models, String query) {
        query = query.toLowerCase();

        final ArrayList<ModelService> filteredModelList = new ArrayList<>();
        for (ModelService model : models) {
            final String text = model.getServiceName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


}

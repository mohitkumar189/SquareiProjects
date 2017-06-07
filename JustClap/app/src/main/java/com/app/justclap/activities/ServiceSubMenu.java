package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterServiceSubMenu;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceSubMenu extends AppCompatActivity implements OnCustomItemClicListener, SearchView.OnQueryTextListener {


    Context context;
    AdapterServiceSubMenu adapterServiceSubMenu;
    ModelService serviceDetail;
    ConnectionDetector cd;
    private RecyclerView mRecyclerView;
    ArrayList<ModelService> service_list;
    String serviceArray = "", catName = "";
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_service_sub_menu);

        context = this;
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

        init();
        service_list = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        serviceArray = in.getExtras().getString("servicearray");
        catName = in.getExtras().getString("categoryName");
        getSupportActionBar().setTitle(catName);

        GridLayoutManager gridlayoutManager = new GridLayoutManager(context, 3);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(gridlayoutManager);

        setListener();
        parseCategoryData();

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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


    private void parseCategoryData() {

        try {

            JSONArray services = new JSONArray(serviceArray);

            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                serviceDetail = new ModelService();
                serviceDetail.setServiceID(jo.getString("serviceID"));
                serviceDetail.setServiceName(jo.getString("serviceName"));
                serviceDetail.setServiceIcon(jo.getString("serviceIcon"));
                service_list.add(serviceDetail);

            }

            adapterServiceSubMenu = new AdapterServiceSubMenu(getApplicationContext(), this, service_list);
            mRecyclerView.setAdapter(adapterServiceSubMenu);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {


        if (flag == 1) {

            ModelService model = adapterServiceSubMenu.getFilter(position);

            Log.e("serviceid", model.getServiceID());
            Intent in = new Intent(ServiceSubMenu.this, QuestionPage.class);
            in.putExtra("servicename", model.getServiceName());
            in.putExtra("serviceid", model.getServiceID());

            startActivity(in);

            // finish();

        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
        }

        return super.onKeyDown(keyCode, event);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service, menu);

        final MenuItem item = menu.findItem(R.id.searchview);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(ServiceSubMenu.this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapterServiceSubMenu.setFilter(service_list);

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
        adapterServiceSubMenu.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<ModelService> filteredModelList = filter(service_list, newText);
        adapterServiceSubMenu.setFilter(filteredModelList);
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

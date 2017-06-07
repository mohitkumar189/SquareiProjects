package com.app.justclap.vendordetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.models.ModelService;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.app.justclap.adapters.AdapterWallet;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelWallet;
import com.app.justclap.utils.AppUtils;

/**
 * Created by admin on 12-02-2016.
 */
public class Vendor_Wallet extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    Context context;
    RecyclerView list_wallet;
    TextView text_balance, text_recharge, text_submit;
    Toolbar toolbar;
    AdapterWallet adapterWallet;
    ModelWallet modelWallet;
    ArrayList<ModelWallet> listWallet;
    ConnectionDetector cd;
    RelativeLayout rl_recharge;
    EditText edit_recharge;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutManager;
    private BroadcastReceiver broadcastReceiver;
    ArrayList<HashMap<String, String>> transactionDetail;
    RelativeLayout rl_main_layout, rl_network;
    String orderId = "";
    String maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int skipCount = 0;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_wallet);

        context = this;
        init();
        listWallet = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListener();
        getWalletDetails();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list_wallet = (RecyclerView) findViewById(R.id.list_wallet);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_wallet.setLayoutManager(layoutManager);
        text_balance = (TextView) findViewById(R.id.text_balance);
        text_recharge = (TextView) findViewById(R.id.text_recharge);
        rl_recharge = (RelativeLayout) findViewById(R.id.rl_recharge);
        edit_recharge = (EditText) findViewById(R.id.edit_recharge);
        text_submit = (TextView) findViewById(R.id.text_submit);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
    }

    private void getWalletDetails() {

        try {
            skipCount = 0;

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                //   AppUtils.showCustomAlert(Vendor_Wallet.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", "0"));

                new AsyncPostDataResponse(Vendor_Wallet.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getWalletDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getWalletDetailsRefresh() {

        try {
            skipCount = 0;
            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                //   AppUtils.showCustomAlert(Vendor_Wallet.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", "0"));

                new AsyncPostDataResponseNoProgress(Vendor_Wallet.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getWalletDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000) + "";

    }

    private void paytmTransaction() {
       /* PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "JustCl35855056476082");
        //paramMap.put("MID", "sameco47740733972704");
        paramMap.put("CUST_ID", AppUtils.getvendorId(context));
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "Justclapwap");
        paramMap.put("TXN_AMOUNT", edit_recharge.getText().toString().trim());
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", AppUtils.getvendorEmail(context));
        paramMap.put("MOBILE_NO", AppUtils.getvendormobile(context));*/

        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "justcl73490598970957");
        paramMap.put("CUST_ID", AppUtils.getvendorId(context));
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail110");
        paramMap.put("WEBSITE", "justclwap");
        paramMap.put("TXN_AMOUNT", edit_recharge.getText().toString().trim());
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", AppUtils.getvendorEmail(context));
        paramMap.put("MOBILE_NO", AppUtils.getvendormobile(context));

        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "https://www.justclapindia.com/paytm_app/generateChecksum.php",
                "https://www.justclapindia.com/paytm_app/verifyChecksum.php");
        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        //Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), inResponse.getString("RESPMSG"), Toast.LENGTH_LONG).show();
                        try {

                           /* String response=inResponse.toString();
                            JSONArray detail=new JSONArray(response);
                            Log.e("transactiondetail",detail.toString());
                            JSONObject jo = detail.getJSONObject(0);
*/
                            Log.e("jobject", inResponse.getString("TXNID") + "********");
                            transactionDetail = new ArrayList<HashMap<String, String>>();

                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("BANKNAME", inResponse.getString("BANKNAME"));
                            hm.put("BANKTXNID", inResponse.getString("BANKTXNID"));
                            hm.put("CURRENCY", inResponse.getString("CURRENCY"));
                            hm.put("GATEWAYNAME", inResponse.getString("GATEWAYNAME"));
                            hm.put("IS_CHECKSUM_VALID", inResponse.getString("IS_CHECKSUM_VALID"));
                            hm.put("MID", inResponse.getString("MID"));
                            hm.put("ORDERID", inResponse.getString("ORDERID"));
                            hm.put("PAYMENTMODE", inResponse.getString("PAYMENTMODE"));
                            hm.put("RESPCODE", inResponse.getString("RESPCODE"));
                            hm.put("RESPMSG", inResponse.getString("RESPMSG"));
                            hm.put("STATUS", inResponse.getString("STATUS"));
                            hm.put("TXNAMOUNT", inResponse.getString("TXNAMOUNT"));
                            hm.put("TXNDATE", inResponse.getString("TXNDATE"));
                            hm.put("TXNID", inResponse.getString("TXNID"));
                            transactionDetail.add(hm);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("transaction_id", hm.get("TXNID")));
                            params.add(new BasicNameValuePair("order_id", orderId));
                            params.add(new BasicNameValuePair("vendorID", AppUtils.getvendorId(context)));
                            params.add(new BasicNameValuePair("status", "1"));
                            params.add(new BasicNameValuePair("TXNAMOUNT", hm.get("TXNAMOUNT")));

                            String url = getString(R.string.url_base_new)
                                    + getString(R.string.rechargeVendorWallet);
                            new AsyncPostDataResponse(Vendor_Wallet.this, 3, params, url);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), inResponse.getString("RESPMSG"), Toast.LENGTH_LONG).show();


                        try {

                           /* String response=inResponse.toString();
                            JSONArray detail=new JSONArray(response);
                            Log.e("transactiondetail",detail.toString());
                            JSONObject jo = detail.getJSONObject(0);
*/
                            Log.e("jobject", inResponse.getString("TXNID") + "********");
                            transactionDetail = new ArrayList<HashMap<String, String>>();

                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("BANKNAME", inResponse.getString("BANKNAME"));
                            hm.put("BANKTXNID", inResponse.getString("BANKTXNID"));
                            hm.put("CURRENCY", inResponse.getString("CURRENCY"));
                            hm.put("GATEWAYNAME", inResponse.getString("GATEWAYNAME"));
                            hm.put("IS_CHECKSUM_VALID", inResponse.getString("IS_CHECKSUM_VALID"));
                            hm.put("MID", inResponse.getString("MID"));
                            hm.put("ORDERID", inResponse.getString("ORDERID"));
                            hm.put("PAYMENTMODE", inResponse.getString("PAYMENTMODE"));
                            hm.put("RESPCODE", inResponse.getString("RESPCODE"));
                            hm.put("RESPMSG", inResponse.getString("RESPMSG"));
                            hm.put("STATUS", inResponse.getString("STATUS"));
                            hm.put("TXNAMOUNT", inResponse.getString("TXNAMOUNT"));
                            hm.put("TXNDATE", inResponse.getString("TXNDATE"));
                            hm.put("TXNID", inResponse.getString("TXNID"));
                            transactionDetail.add(hm);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("transaction_id", hm.get("TXNID")));
                            params.add(new BasicNameValuePair("order_id", orderId));
                            params.add(new BasicNameValuePair("vendorID", AppUtils.getvendorId(context)));
                            params.add(new BasicNameValuePair("status", "0"));
                            params.add(new BasicNameValuePair("TXNAMOUNT", "0"));

                            String url = getString(R.string.url_base_new)
                                    + getString(R.string.rechargeVendorWallet);
                            new AsyncPostDataResponse(Vendor_Wallet.this, 4, params, url);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }

    private void setListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getWalletDetailsRefresh();
            }
        });

        text_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_recharge.setVisibility(View.VISIBLE);
                edit_recharge.setText("");
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);

            }

        });

        text_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edit_recharge.getText().toString().equalsIgnoreCase("")) {
                    rl_recharge.setVisibility(View.GONE);
                    initOrderId();
                    paytmTransaction();
                } else {
                    AppUtils.showCustomAlert(Vendor_Wallet.this, "Please enter recharge amount");
                }
            }
        });

        list_wallet.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(listWallet.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    modelWallet = new ModelWallet();
                                    modelWallet.setRowType(2);
                                    listWallet.add(modelWallet);
                                    adapterWallet.notifyDataSetChanged();

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "vendorID", AppUtils.getvendorId(context)));


                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));

                                        new AsyncPostDataResponseNoProgress(context, 4, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getWalletDetails));

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    } else {

                        Log.e("maxlength", "*" + listWallet.size());
                    }
                }
            }
        });


    }


    @Override
    public void onItemClickListener(int position, int flag) {


    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    listWallet.removeAll(listWallet);
                    maxlistLength = commandResult.getString("total");
                    text_balance.setText("Rs. " + data.getString("Amount"));
                    JSONArray Wallet = data.getJSONArray("Wallet");

                    for (int i = 0; i < Wallet.length(); i++) {

                        JSONObject jo = Wallet.getJSONObject(i);
                        //   Log.e("services", "*" + jo.length());
                        modelWallet = new ModelWallet();
                        modelWallet.setCredit(jo.getString("credit"));
                        modelWallet.setDebit(jo.getString("debit"));
                        modelWallet.setRowType(1);
                        modelWallet.setBalanceAmount(jo.getString("balanceAmount"));
                        modelWallet.setCreatedDate(jo.getString("createdDate"));
                        modelWallet.setCheckRecharge(jo.getString("checkRecharge"));
                        modelWallet.setCustomerImage(jo.getString("customerImage"));
                        modelWallet.setSearch_id(jo.getString("search_id"));
                        modelWallet.setWalletID(jo.getString("walletID"));
                        modelWallet.setServiceName(jo.getString("serviceName"));
                        modelWallet.setCustomerName(jo.getString("customerName"));
                        if (!jo.getString("walletID").equalsIgnoreCase("")) {

                            listWallet.add(modelWallet);
                        }

                    }
                    adapterWallet = new AdapterWallet(context, this, listWallet);
                    list_wallet.setAdapter(adapterWallet);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }


                }

            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    text_balance.setText(data.getString("Amount"));
                    listWallet.remove(listWallet.size() - 1);
                    JSONArray Wallet = data.getJSONArray("Wallet");

                    for (int i = 0; i < Wallet.length(); i++) {

                        JSONObject jo = Wallet.getJSONObject(i);
                        //   Log.e("services", "*" + jo.length());
                        modelWallet = new ModelWallet();
                        modelWallet.setCredit(jo.getString("credit"));
                        modelWallet.setDebit(jo.getString("debit"));
                        modelWallet.setRowType(1);
                        modelWallet.setBalanceAmount(jo.getString("balanceAmount"));
                        modelWallet.setCreatedDate(jo.getString("createdDate"));
                        modelWallet.setCheckRecharge(jo.getString("checkRecharge"));
                        modelWallet.setCustomerImage(jo.getString("customerImage"));
                        modelWallet.setSearch_id(jo.getString("search_id"));
                        modelWallet.setWalletID(jo.getString("walletID"));
                        modelWallet.setServiceName(jo.getString("serviceName"));
                        modelWallet.setCustomerName(jo.getString("customerName"));
                        if (!jo.getString("walletID").equalsIgnoreCase("")) {

                            listWallet.add(modelWallet);
                        }

                    }

                    if (Wallet.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                    loading = true;
                    adapterWallet.notifyDataSetChanged();

                } else {

                    adapterWallet.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                }

            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    text_balance.setText(data.getString("wallet"));

                    getWalletDetailsRefresh();
                }

            }


        } catch (JSONException e) {
            if (position == 4) {
                skipCount = skipCount - 10;
                loading = true;
            }
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }

            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(Vendor_Wallet.this, getResources().getString(R.string.problem_server));
    }


}

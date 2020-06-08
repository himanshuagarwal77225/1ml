package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterNotificationList;
import com.medical.product.adapter.AdapterOrderList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetMyNotificationListModel;
import com.medical.product.models.GatterGetMyOrderListModel;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.medical.product.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class MyNotificationActivity extends AppCompatActivity {
    private static TextView textNotifycart;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recycleviewnotification;
    private AdapterNotificationList recyclerAdapter;
    LinearLayout layEmptynoti;
    private ArrayList<GatterGetMyNotificationListModel> gatterGetMyNotificationListModels;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynotification);
        layEmptynoti = (LinearLayout) findViewById(R.id.layEmptynoti);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycleviewnotification = (RecyclerView) findViewById(R.id.recycleviewnotification);
        Utlity.clear_notification(this);

        loadDataProfile();

    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")

        getnotification();
    }


    private void getnotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.notifications,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                gatterGetMyNotificationListModels = new Gson().fromJson(object.getJSONArray("notification").toString(), new TypeToken<List<GatterGetMyNotificationListModel>>() {
                                }.getType());
                                recyclerAdapter = new AdapterNotificationList(gatterGetMyNotificationListModels, MyNotificationActivity.this);
                                LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
                                recycleviewnotification.setLayoutManager(recyclerlayoutManager);
                                recycleviewnotification.setHasFixedSize(true);
                                recycleviewnotification.setAdapter(recyclerAdapter);
                                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                                itemTouchHelper.attachToRecyclerView(recycleviewnotification);

                            }
                            if (gatterGetMyNotificationListModels.size() == 0) {
                                layEmptynoti.setVisibility(View.VISIBLE);
                                recycleviewnotification.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        layEmptynoti.setVisibility(View.VISIBLE);
                        recycleviewnotification.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("user_id", store.get("id"));
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(MyNotificationActivity.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            if (swipeDir == ItemTouchHelper.RIGHT) {
                int position = viewHolder.getAdapterPosition();
                gatterGetMyNotificationListModels.remove(position);
                recyclerAdapter.notifyDataSetChanged();
                if (gatterGetMyNotificationListModels.size() == 0) {
                    layEmptynoti.setVisibility(View.VISIBLE);
                    recycleviewnotification.setVisibility(View.GONE);
                }
            } else {
                recyclerAdapter.notifyDataSetChanged();

            }

        }
    };
}

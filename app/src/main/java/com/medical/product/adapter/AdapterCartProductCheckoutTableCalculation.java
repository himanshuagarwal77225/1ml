package com.medical.product.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.medical.product.helpingFile.ReuseMethod.retstring;


public class AdapterCartProductCheckoutTableCalculation extends RecyclerView.Adapter<AdapterCartProductCheckoutTableCalculation.MyViewHolder> {

    SharedPreferences prefrance,prefranceid;
    String sharid;
     ArrayList<GatterGetCartList> recyclerModels; // this data structure carries our title and description
     Context context;
    public AdapterCartProductCheckoutTableCalculation(ArrayList<GatterGetCartList> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_check_table, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final GatterGetCartList GatterGetCartList = recyclerModels.get(position);
        Log.i("asdf","position========"+position);

     /*   holder.txtsubtotal.setText(GatterGetCartList.getId());
        holder.txtshipcharge.setText(GatterGetCartList.getId());
        holder.txtdiscount.setText(GatterGetCartList.getId());
        holder.txtprderprice.setText(GatterGetCartList.getId());*/

       /* holder.txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
               Log.i("get","getaftertextchange"+s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });*/


  /*      holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] array={GatterGetCartList.getId()};
                String operation="remove";
                String rooturl="http://demo.ybisoftech.com/1ml/api/product/removecart";

                Map<String, String> params = new HashMap<>();
                params.put("id", GatterGetCartList.getId());
                addToCardDataHandling(operation,rooturl,params,position);
            }
        });*/


    }



    @Override
    public int getItemCount() {
        return recyclerModels.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
       // return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtsubtotal,txtshipcharge,txtdiscount,txtprderprice;

        MyViewHolder(View view) {
            super(view);
            txtsubtotal=(TextView)itemView.findViewById(R.id.txtsubtotal);
            txtshipcharge=(TextView)itemView.findViewById(R.id.txtshipcharge);
            txtdiscount=(TextView)itemView.findViewById(R.id.txtdiscount);
            txtprderprice=(TextView)itemView.findViewById(R.id.txtprderprice);


        }

    }

    public void filterList(ArrayList<GatterGetCartList> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }

    public String addToCardDataHandling(final String operation, final String rooturl, final Map<String, String> array, final int position) {

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, rooturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("ass","notifyDataSetChanged==============="+response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("false")) {
                                Toast.makeText(context, obj.getString("cart"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, obj.getString("cart"), Toast.LENGTH_SHORT).show();
                                delete(position);

                            }
                        }
                        catch (JSONException e)
                        {

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)

                           // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("sdfads", "profile error=========" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if(operation.equals("remove"))
                {
                    params=array;

                    Log.i("sdfads", "profile error=========" + params.get("id"));


                }


           /* params.put("email", stremail);
            params.put("phone", strphone);*/
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

        return retstring;
    }


    public void delete(int position) {
        recyclerModels.remove(position);
        notifyItemRemoved(position);
    }



}

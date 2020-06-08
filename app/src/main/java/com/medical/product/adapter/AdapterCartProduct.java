package com.medical.product.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.medical.product.Ui.CartActivity;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.medical.product.helpingFile.ReuseMethod.retstring;


public class AdapterCartProduct extends RecyclerView.Adapter<AdapterCartProduct.MyViewHolder> {
    Keystore store;
    SharedPreferences prefrance, prefranceid;
    String sharid;
    ArrayList<GatterGetCartList> recyclerModels; // this data structure carries our title and description
    Context context;
    String qty = "";

    String[] quantityarray = {"", "1", "2", "3", "4", "5", "more"};
    SpinnerCustomQuantityAdapter customAdapter;

    public AdapterCartProduct(ArrayList<GatterGetCartList> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // update your data here
        final GatterGetCartList GatterGetCartList = recyclerModels.get(position);

        holder.txtTitlehorizontal.setText(GatterGetCartList.getMed_name());
        holder.txtsellername.setText(GatterGetCartList.getVendor_name());
        holder.txtQty.setText(GatterGetCartList.getQuentity());
        holder.companyname.setText(GatterGetCartList.getCompany_name());

        Double persent = FormulasCalculation.CalculateProductDiscount(Double.parseDouble(GatterGetCartList.getMrp()), Double.parseDouble(GatterGetCartList.getSale_price()));

        holder.txtPriceOffer.setText(String.valueOf(persent) + "% off");

//        if (!TextUtils.isEmpty(GatterGetCartList.getAdditional_offer_start())) {
//            if (GatterGetCartList.getAdditional_offer_type().equalsIgnoreCase("percent")) {
//                Double quentity = Double.parseDouble(GatterGetCartList.getQuentity());
//                Double newpricecheck = Double.parseDouble(GatterGetCartList.getSale_price());
//                Double addoffer = Double.parseDouble(GatterGetCartList.getAdditional_offer_percentage());
//                Double price = newpricecheck * quentity;
//                Double newprice = ((price * addoffer) / 100);
//                holder.txtNewPrice.setText(context.getString(R.string.Rs) + String.valueOf(price - newprice));
//                holder.txtAddoffApplyed.setVisibility(View.VISIBLE);
//                holder.txtAddoffApplyed.setText(String.valueOf(addoffer) + " flat additional discount offer Applied");
//            } else {
//                Double quentity = Double.parseDouble(GatterGetCartList.getQuentity());
//                Double newpricecheck = Double.parseDouble(GatterGetCartList.getSale_price());
//                Double addoffer = Double.parseDouble(GatterGetCartList.getAdditional_offer_percentage());
//                Double price = newpricecheck * quentity;
//                holder.txtNewPrice.setText(context.getString(R.string.Rs) + String.valueOf(price - addoffer));
//                holder.txtAddoffApplyed.setVisibility(View.VISIBLE);
//                holder.txtAddoffApplyed.setText(String.valueOf(addoffer) + " % additional discount offer Applied");
//            }
//        } else {
            Double quentity = Double.parseDouble(GatterGetCartList.getQuentity());
            Double newpricecheck = Double.parseDouble(GatterGetCartList.getSale_price());
            Double newprice = newpricecheck * quentity;
            holder.txtNewPrice.setText(context.getString(R.string.Rs) + String.valueOf(newprice));

//        }

        Double quentity2 = Double.parseDouble(GatterGetCartList.getQuentity());
        Double newpricecheck2 = Double.parseDouble(GatterGetCartList.getMrp());
        Double newprice2 = newpricecheck2 * quentity2;

        holder.txtOldProce.setText(String.valueOf(newprice2));

        if (!GatterGetCartList.getImage().equals("")) {
            Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + GatterGetCartList.getImage()).into(holder.ivThumb);
        }

        holder.txtQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Product_detailActivity.class);
                intent.putExtra("medname", GatterGetCartList.getMed_name());
                intent.putExtra("med_id", GatterGetCartList.getPrdct_id());
                context.startActivity(intent);

            }
        });


        holder.spinnerProductQuantity.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        holder.spinnerProductQuantity.setPrompt("Select gender...");
        customAdapter = new SpinnerCustomQuantityAdapter(context, quantityarray);
        holder.spinnerProductQuantity.setAdapter(customAdapter);


        holder.spinnerProductQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 6) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("Enter Quentity");

                    final EditText input = new EditText(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setText(holder.txtQty.getText());
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    qty = input.getText().toString();
                                    String operation = "changeQuantity";
                                    String rooturl = ApiFileuri.ROOT_HTTP_URL + "product/quentity";
                                    Map<String, String> params = new HashMap<>();
                                    params.put("id", GatterGetCartList.getCart_id());
                                    params.put("quentity", input.getText().toString());
                                    addToCardDataHandling(operation, rooturl, params, position, holder);
                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();

                } else if (pos == 0) {

                } else {
                    qty = quantityarray[pos];
                    String operation = "changeQuantity";
                    String rooturl = ApiFileuri.ROOT_HTTP_URL + "product/quentity";
                    Map<String, String> params = new HashMap<>();
                    params.put("id", GatterGetCartList.getCart_id());
                    params.put("quentity", quantityarray[pos]);
                    addToCardDataHandling(operation, rooturl, params, position, holder);
                   /*


                    holder.txtQty.setText(quantityarray[pos]);
                    Double newpricecheck=Double.parseDouble(GatterGetCartList.getSale_price());
                    Double price= newpricecheck * Double.parseDouble(quantityarray[pos]);
                    if(!(GatterGetCartList.getAdditional_offer_start().equals("") || GatterGetCartList.getAdditional_offer_start().equals("null") || GatterGetCartList.getAdditional_offer_start().equals(null)))
                    {
                        Double addoffer=Double.parseDouble(GatterGetCartList.getAdditional_offer_percentage());
                        Double newprice= ((price*addoffer)/100);
                        holder.txtNewPrice.setText( String.valueOf(price-newprice));
                    }
                    else {
                        holder.txtNewPrice.setText( String.valueOf(price));
                    }*/
                }
                holder.spinnerProductQuantity.setSelection(0);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                holder.spinnerProductQuantity.setSelection(0);
            }


        });


        holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store = Keystore.getInstance(context);
                String[] array = {GatterGetCartList.getCart_id()};
                String operation = "remove";
                String rooturl = ApiFileuri.ROOT_HTTP_URL + "product/removecart";
                Map<String, String> params = new HashMap<>();
                params.put("id", GatterGetCartList.getCart_id());
                if (!store.get("id").equals("")) {
                    params.put("user_id", store.get("id"));
                } else {
                    params.put("macid", ReuseMethod.MacAddress(context));
                }
                addToCardDataHandling(operation, rooturl, params, position, holder);
            }
        });

        holder.txtTitlehorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Product_detailActivity.class);
                intent.putExtra("medname", GatterGetCartList.getMed_name());
                intent.putExtra("med_id", GatterGetCartList.getPrdct_id());
                context.startActivity(intent);
            }
        });


    }

   /* private void methodGetPrice(MyViewHolder holder, int position) {
        if(recyclerModels.get(position).getOffer().equals("")){
            //int inttotprice= (Integer.parseInt(recyclerModels.get(position).getPrice()))*(Integer.parseInt(recyclerModels.get(position).getQuentity()));

            int inttotprice= (Integer.parseInt(recyclerModels.get(position).getPrice()));
            int intqty= (Integer.parseInt(recyclerModels.get(position).getQuentity()));

          String strt= String.valueOf(inttotprice*intqty);
            Log.i("less","lessprice=========inttotprice===="+strt);
            holder.txtNewPrice.setText(strt);
            holder.txtOldProce.setVisibility(View.GONE);
       //     holder.txtOldProce.setText("sadfasdfas");
         //   holder.txtNewPrice.setText("sadfsdafsdf");
        }


        else if( !recyclerModels.get(position).getOffer().equals("") || !recyclerModels.get(position).getOffer().equals("null"))
        {
            int intoffer= Integer.parseInt(recyclerModels.get(position).getOffer());

            int inttotprice= (Integer.parseInt(recyclerModels.get(position).getPrice()))*(Integer.parseInt(recyclerModels.get(position).getQuentity()));

            int priceless = (int)(inttotprice*(intoffer/100.0f));

            int newprice=inttotprice-priceless;

            holder.txtOldProce.setText(String.valueOf(inttotprice));
            holder.txtNewPrice.setText(String.valueOf(newprice));
        }
    }
*/


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
        TextView txtTitlehorizontal, companyname, txtCutRate, txtPriceOffer, txtAddPrice, txtQuantity, txtInStock, txtNewPrice, txtOldProce, txtsellername, txtQty, txtAddoffApplyed;
        Button BtnQuentityMinus, BtnQuentityPlus;
        ImageView removeProduct;
        LinearLayout layQuantity;
        Spinner spinnerProductQuantity;

        ImageView ivThumb;

        MyViewHolder(View view) {
            super(view);
            txtTitlehorizontal = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            txtNewPrice = (TextView) itemView.findViewById(R.id.txtNewPrice);
            txtsellername = (TextView) itemView.findViewById(R.id.txtsellername);
            // txtCutRate=(TextView)itemView.findViewById(R.id.txtCutRate);
            txtPriceOffer = (TextView) itemView.findViewById(R.id.txtPriceOffer);
            txtOldProce = (TextView) itemView.findViewById(R.id.txtOldProce);
            txtInStock = (TextView) itemView.findViewById(R.id.txtInStock);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            spinnerProductQuantity = (Spinner) itemView.findViewById(R.id.spinnerProductQuantity);
            removeProduct = (ImageView) itemView.findViewById(R.id.removeProduct);
            layQuantity = (LinearLayout) itemView.findViewById(R.id.layQuantity);
            txtAddoffApplyed = (TextView) itemView.findViewById(R.id.txtAddoffApplyed);
            companyname = (TextView) itemView.findViewById(R.id.companyname);

        }

    }

    public void filterList(ArrayList<GatterGetCartList> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass", "notifyDataSetChanged===============");
        notifyDataSetChanged();
    }

    public String addToCardDataHandling(final String operation, final String rooturl, final Map<String, String> array, final int position, final MyViewHolder holder) {

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, rooturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("ass", "notifyDataSetChanged===============" + response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("false")) {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                if (operation.equals("changeQuantity")) {


                                    recyclerModels.get(position).setQuentity(qty);

                                    holder.txtQty.setText(recyclerModels.get(position).getQuentity());

                                    String qtyii = recyclerModels.get(position).getQuentity();

                                    Double newpricecheck = Double.parseDouble(recyclerModels.get(position).getSale_price());
                                    Double price = newpricecheck * Double.parseDouble(qtyii);

                                    if (!TextUtils.isEmpty(recyclerModels.get(position).getAdditional_offer_start())) {
                                        Double addoffer = Double.parseDouble(recyclerModels.get(position).getAdditional_offer_percentage());
                                        Double newprice = ((price * addoffer) / 100);
                                        holder.txtNewPrice.setText(String.valueOf(price - newprice));
                                    } else {
                                        holder.txtNewPrice.setText(String.valueOf(price));
                                    }

                                    Toast.makeText(context, "quantity Change", Toast.LENGTH_SHORT).show();


                                    // holder.spinner_qty.setPrompt(qty);

                                    notifyDataSetChanged();
                                    //  methodGetPrice(holder,position);
                                    if (context instanceof CartActivity) {
                                        ((CartActivity) context).CalculateGrandTotal();
                                    }

                                } else if (operation.equals("remove")) {
                                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    delete(position);
                                }


                            }
                        } catch (JSONException e) {

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

                if (operation.equals("remove")) {
                    params = array;
                    Log.i("sdfads", "profile error=========" + params.get("id"));
                } else if (operation.equals("changeQuantity")) {
                    params = array;
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
        if (context instanceof CartActivity) {
            ((CartActivity) context).CalculateGrandTotal();
        }

        ReuseMethod.SetTotalCartItem(context, String.valueOf(recyclerModels.size()));


    }

}

package com.medical.product.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.medical.product.R;
import com.medical.product.Ui.CartActivity;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.LabCartActivity;
import com.medical.product.Ui.LabTestMainActivity;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;
import com.medical.product.models.GatterGetLabCartList;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.medical.product.helpingFile.ReuseMethod.retstring;

public class AdapterLabCartProduct extends FirestoreRecyclerAdapter<GatterGetLabCartList, AdapterLabCartProduct.LabHolder> {
    private static final String TAG = "AdapterLabCartProduct";
    private OnItemClickListener listener;
    Context mContext;
    String phone;
    float actAmount=0;
    float fixAmount=0;
    int temp;
    float discAmount=0;
    Dialog dialog1;
    Boolean isRadio1alreadyChecked=false;
    Boolean isRadio2alreadyChecked=false;
    Boolean isRadio3alreadyChecked=false;
    Boolean isRadio4alreadyChecked=false;
    float additional_discountInt=0;
    float multilpe=1;
    Date currentTime;
    TextView actamount,fixamount,discamount;
    ArrayList<String> product = new ArrayList<>();
    ProgressDialog progressDialog;
    View v;
    public AdapterLabCartProduct(@NonNull FirestoreRecyclerOptions<GatterGetLabCartList> options,Context context,String phone) {
        super(options);
        this.mContext=context;
        this.phone=phone;
        currentTime = Calendar.getInstance().getTime();
    }

    @Override
    protected void onBindViewHolder(@NonNull LabHolder LabHolder, final int i, @NonNull final GatterGetLabCartList gatterGetLabCartList) {
        additional_discountInt = Float.parseFloat(gatterGetLabCartList.getAdditional_discount());
        multilpe=Float.parseFloat(gatterGetLabCartList.getMultiple());
        LabHolder.name.setText(gatterGetLabCartList.getProduct());
        LabHolder.price.setText("₹ "+gatterGetLabCartList.getPrice());
        LabHolder.discount.setText("Save "+gatterGetLabCartList.getDiscount()+"%OFF");
        LabHolder.strike.setText(ReuseMethod.discount_offer_strike(Double.parseDouble(gatterGetLabCartList.getPrice()),Double.parseDouble(gatterGetLabCartList.getDiscount())));
        LabHolder.strike.setPaintFlags(LabHolder.strike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        LabHolder.plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInfo();
            }
        });

        LabHolder.radioPlanButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRadio1alreadyChecked){
                    isRadio1alreadyChecked=true;
                    isRadio2alreadyChecked=false;
                    isRadio3alreadyChecked=false;
                    isRadio4alreadyChecked=false;
                                        Log.d(TAG, "onClick: radio1 called");
                    additional_discountInt = Integer.parseInt(gatterGetLabCartList.getAdditional_discount());
                    multilpe=Integer.parseInt(gatterGetLabCartList.getMultiple());
                    updatePlan(currentTime.toString(),"yes", "monthly", gatterGetLabCartList.getProduct(),
                            "40","12");
                    changeDiscount(Float.parseFloat(gatterGetLabCartList.getPrice()),
                            Float.parseFloat(gatterGetLabCartList.getDiscount()), additional_discountInt, 40,12,multilpe);
                    additional_discountInt = 40;
                    multilpe = 12;
                }
            }
        });
        LabHolder.radioPlanButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRadio2alreadyChecked){
                    isRadio2alreadyChecked=true;
                    isRadio1alreadyChecked=false;
                    isRadio3alreadyChecked=false;
                    isRadio4alreadyChecked=false;
                                        Log.d(TAG, "onClick: radio 2 called");
                    additional_discountInt = Integer.parseInt(gatterGetLabCartList.getAdditional_discount());
                    multilpe=Integer.parseInt(gatterGetLabCartList.getMultiple());
                    updatePlan(currentTime.toString(),"yes", "quaterly", gatterGetLabCartList.getProduct(),
                            "30","3");
                    changeDiscount(Float.parseFloat(gatterGetLabCartList.getPrice()),
                            Float.parseFloat(gatterGetLabCartList.getDiscount()), additional_discountInt, 30,3,multilpe);
                    additional_discountInt = 30;
                    multilpe = 3;

                }
            }
        });
        LabHolder.radioPlanButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRadio3alreadyChecked){
                    isRadio3alreadyChecked=true;
                    isRadio2alreadyChecked=false;
                    isRadio1alreadyChecked=false;
                                        isRadio4alreadyChecked=false;
                    Log.d(TAG, "onClick: radio 3 called");
                    additional_discountInt = Integer.parseInt(gatterGetLabCartList.getAdditional_discount());
                    multilpe=Integer.parseInt(gatterGetLabCartList.getMultiple());
                    updatePlan(currentTime.toString(),"yes", "halfyearly", gatterGetLabCartList.getProduct(),
                            "20","2");
                    changeDiscount(Float.parseFloat(gatterGetLabCartList.getPrice()),
                            Float.parseFloat(gatterGetLabCartList.getDiscount()), additional_discountInt, 20,2,multilpe);
                    additional_discountInt = 20;
                    multilpe=2;

                }
            }
        });
        LabHolder.radioPlanButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRadio4alreadyChecked){
                    isRadio4alreadyChecked=true;
                    isRadio2alreadyChecked=false;
                    isRadio3alreadyChecked=false;
                    isRadio1alreadyChecked=false;
                                        Log.d(TAG, "onClick: radio4 called");
                    additional_discountInt = Integer.parseInt(gatterGetLabCartList.getAdditional_discount());
                    multilpe=Integer.parseInt(gatterGetLabCartList.getMultiple());
                    updatePlan(currentTime.toString(),"yes", "annually", gatterGetLabCartList.getProduct(),
                            "00","1");
                    changeDiscount(Float.parseFloat(gatterGetLabCartList.getPrice()),
                            Float.parseFloat(gatterGetLabCartList.getDiscount()), additional_discountInt, 0,1,multilpe);
                    additional_discountInt = 0;
                    multilpe=1;


                }
            }
        });
        if(gatterGetLabCartList.getPlan_status().equals("yes")){
                        Log.d(TAG, "onBindViewHolder: status : "+gatterGetLabCartList.getPlan_status());
            Log.d(TAG, "onBindViewHolder: additional discount : "+gatterGetLabCartList.getAdditional_discount());
            Log.d(TAG, "onBindViewHolder: plan value :"+gatterGetLabCartList.getPlan_value());
            if (!gatterGetLabCartList.getAdditional_discount().equals("00")) {
                additional_discountInt = Float.parseFloat(gatterGetLabCartList.getAdditional_discount());
                multilpe=Float.parseFloat(gatterGetLabCartList.getMultiple());
                Log.d(TAG, "onBindViewHolder: called "+additional_discountInt);
                switch (gatterGetLabCartList.getPlan_value()) {
                    case "monthly":
                        LabHolder.radioPlanGroup.check(R.id.radio1);
                        Log.d(TAG, "onBindViewHolder: called monthly");
                        break;
                    case "quaterly":
                        LabHolder.radioPlanGroup.check(R.id.radio2);
                        break;
                    case "halfyearly":
                        LabHolder.radioPlanGroup.check(R.id.radio3);
                        break;
                    case "annually":
                        LabHolder.radioPlanGroup.check(R.id.radio4);
                        break;

                }
            }else{
                               if(LabHolder.radioPlanGroup.getCheckedRadioButtonId()!=-1){
                    Log.d(TAG, "onBindViewHolder !00 : clear check called");
                    LabHolder.radioPlanGroup.clearCheck();
                }
            }
        }else{

            if(LabHolder.radioPlanGroup.getCheckedRadioButtonId()!=-1){
                Log.d(TAG, "onBindViewHolder yes or no: clear check called");
                LabHolder.radioPlanGroup.clearCheck();
            }
        }
        LabHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LabHolder.radioPlanGroup.getCheckedRadioButtonId()!=-1) {

                    Log.d(TAG, "onClick cancel: called");
                    additional_discountInt = Float.parseFloat(gatterGetLabCartList.getAdditional_discount());
                    multilpe=Float.parseFloat(gatterGetLabCartList.getMultiple());
                    updatePlan(currentTime.toString(),"no", "no", gatterGetLabCartList.getProduct(), "00","1");
                    changeDiscount(Float.parseFloat(gatterGetLabCartList.getPrice()),
                            Float.parseFloat(gatterGetLabCartList.getDiscount()), additional_discountInt, 0,1,multilpe);
                    additional_discountInt = 0;
                    multilpe=1;

                }
            }
        });
        LabHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick delete : i "+ i);

                updatePlan(currentTime.toString(),"no", "no", gatterGetLabCartList.getProduct(), "00","1");
                getSnapshots().getSnapshot(i).getReference().delete();
                product.remove(gatterGetLabCartList.getProduct());
                additional_discountInt= Float.parseFloat(gatterGetLabCartList.getAdditional_discount());
                multilpe=Float.parseFloat(gatterGetLabCartList.getMultiple());
                Log.d(TAG, "onClick delete: additional discount: " + additional_discountInt);
                minusGrnd(Float.parseFloat(gatterGetLabCartList.getPrice()),Float.parseFloat(gatterGetLabCartList.getDiscount())
                        ,additional_discountInt,multilpe);
                listener.onItemClick(getSnapshots().getSnapshot(i),i,gatterGetLabCartList.getProduct());

            }
        });
        if(!product.contains(gatterGetLabCartList.getProduct())) {
            product.add(gatterGetLabCartList.getProduct());
            grandCalculate(Float.parseFloat(gatterGetLabCartList.getPrice()),
                    Float.parseFloat(gatterGetLabCartList.getDiscount()),additional_discountInt,multilpe);
        }
        if(!gatterGetLabCartList.getImageUrl().equals("No")) {
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(gatterGetLabCartList.getImageUrl())
                    .into(LabHolder.image);
        }else{
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(R.mipmap.ic_launcher)
                    .into(LabHolder.image);
        }
    }
    private void changeDiscount(float fixvalue,float discvalue,float init_discount,float fin_discount,float fin_multiple,float ini_multiple){
        Log.d(TAG, "changeDiscount: called");
        minusGrnd(fixvalue,discvalue,init_discount,ini_multiple);

        grandCalculate(fixvalue,discvalue,fin_discount,fin_multiple);



    }
    private void minusGrnd(float fixvalue,float discvalue,float adisc,float in_multiple){
        Log.d(TAG, "minusGrnd: called ");

        float value = Float.parseFloat(ReuseMethod.discount_offer_strike(Double.valueOf(fixvalue),Double.valueOf(discvalue)).substring(1));
        fixvalue=ReuseMethod.additionalDiscout(fixvalue,adisc,in_multiple);

        actAmount=actAmount-fixvalue;
        fixAmount=fixAmount-value;
        discAmount=fixAmount-actAmount;
        Log.d(TAG, "minusGrnd: fixvalue"+fixvalue);
        Log.d(TAG, "minusGrnd: discvalue"+adisc);
        Log.d(TAG, "minusGrnd: actual amount : "+actAmount);
        Log.d(TAG, "minusGrnd: fix amount : "+fixAmount);
        Log.d(TAG, "minusGrnd: disc amount : "+discAmount);
        setValue();
    }
    private void grandCalculate(float fixvalue,float discvalue,float adisc,float fin_multiple){
        Log.d(TAG, "grandCalculate: called");
        float value = Float.parseFloat(ReuseMethod.discount_offer_strike(Double.valueOf(fixvalue),Double.valueOf(discvalue)).substring(1));
        fixvalue=ReuseMethod.additionalDiscout(fixvalue,adisc,fin_multiple);

        actAmount=actAmount+fixvalue;
        fixAmount=fixAmount+value;
        discAmount=fixAmount-actAmount;
        Log.d(TAG, "grandCalculate: fixvalue"+fixvalue);
        Log.d(TAG, "grandCalculate: discvalue"+adisc);
        Log.d(TAG, "grandCalculate: actual amount : "+actAmount);
        Log.d(TAG, "grandCalculate: fix amount : "+fixAmount);
        Log.d(TAG, "grandCalculate: disc amount : "+discAmount);
        setValue();
    }
    private void setValue(){
        Log.d(TAG, "setValue: called");
        actamount = ((Activity)mContext).findViewById(R.id.txtFinalPrice);
        actamount.setText("Rs "+ actAmount);
        fixamount = ((Activity)mContext).findViewById(R.id.txtActPrice);
        fixamount.setText("Rs "+fixAmount);
        discamount = ((Activity)mContext).findViewById(R.id.totalSavingamount);
        discamount.setText("Rs "+discAmount);
    }

    @NonNull
    @Override
    public LabHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v =LayoutInflater.from(parent.getContext()).inflate(R.layout.labcart_test_item, parent, false);
        return new LabHolder(v);
    }

    class LabHolder extends RecyclerView.ViewHolder{
        TextView name,price,discount,strike,plan;
        ImageView delete,cancel,image;
        private RadioGroup radioPlanGroup;
        private RadioButton radioPlanButton1,radioPlanButton2,radioPlanButton3,radioPlanButton4;


        public LabHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.test_name_labcart);
            price=itemView.findViewById(R.id.test_price_labcart);
            discount = itemView.findViewById(R.id.test_discount_labcart);
            delete=itemView.findViewById(R.id.delete);
            radioPlanGroup = itemView.findViewById(R.id.radio_group);
            strike=itemView.findViewById(R.id.test_strike_labcart);
            plan=itemView.findViewById(R.id.plan_info);
            radioPlanButton1=itemView.findViewById(R.id.radio1);
            radioPlanButton2=itemView.findViewById(R.id.radio2);
            radioPlanButton3=itemView.findViewById(R.id.radio3);
            radioPlanButton4=itemView.findViewById(R.id.radio4);
            cancel=itemView.findViewById(R.id.plan_cancel);
            image=itemView.findViewById(R.id.imageUrl);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position, String product);
    }

    public void setOnItemClickListener(OnItemClickListener listener ){
        this.listener=listener;
    }

    public void createInfo(){
        if(dialog1!=null){
            if(dialog1.isShowing()) {
                dialog1.dismiss();

            }
            dialog1 = null;
        }
        dialog1 = new Dialog(mContext);
        dialog1.setContentView(R.layout.plan_dialog);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.show();
    }

    public void updatePlan(String time,String plan_status,String plan_value,String product,String additional_discount,String multiple){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait, Adding to cart");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        ReuseMethod.updateLabCartPlan(multiple,time,additional_discount,mContext,plan_status,plan_value,progressDialog,product);
    }
}

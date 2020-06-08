package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class OrderPrescriptionInfoActivity extends AppCompatActivity {
    private Keystore store;
    String phone="";

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    Button btnContinue;
    LinearLayout layorderall;
    EditText edtSpicifiy,edtDays;
    RadioButton radduration,raddoctor;
    String insert_id="";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_prescription);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
        phone  = store.get("phone");
       setTitle("Prescription options");
        Intent intent = getIntent();
         insert_id = intent.getStringExtra("insert_id");
        radioGroup = (RadioGroup) findViewById(R.id.radGroupChooseOption);
        btnContinue= (Button) findViewById(R.id.btnContinue);
        layorderall= (LinearLayout) findViewById(R.id.layorderall);
        edtSpicifiy= (EditText) findViewById(R.id.edtSpicifiy);
        edtDays= (EditText) findViewById(R.id.edtDays);
        radduration= (RadioButton) findViewById(R.id.radduration);
        raddoctor= (RadioButton) findViewById(R.id.raddoctor);

        radduration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean checked = ((RadioButton) buttonView).isChecked();
                if(checked==true)
                {
                    raddoctor.setChecked(false);
                }
            }
        });

        raddoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean checked = ((RadioButton) buttonView).isChecked();
                if(checked==true)
                {
                    radduration.setChecked(false);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radorderall)
                {
                    layorderall.setVisibility(View.VISIBLE);
                    edtSpicifiy.setVisibility(View.GONE);
                }
                else if(checkedId==R.id.radletmespecifiy)
                {
                    layorderall.setVisibility(View.GONE);
                    edtSpicifiy.setVisibility(View.VISIBLE);

                }
                else if(checkedId==R.id.raCallme)
                {
                    layorderall.setVisibility(View.GONE);
                    edtSpicifiy.setVisibility(View.GONE);
                }
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String PrescriptionChoose="";
                String selectnum="";
                if(selectedId==R.id.radorderall)
                {

                    if(radduration.isChecked())
                    {
                        PrescriptionChoose="For "+edtDays.getText().toString()+" days";
                        selectnum="0";
                    }

                    if(raddoctor.isChecked())
                    {
                        PrescriptionChoose=raddoctor.getText().toString();
                        selectnum="0";
                    }
                }
                else if(selectedId==R.id.radletmespecifiy)
                {
                    selectnum="1";

                    PrescriptionChoose=edtSpicifiy.getText().toString();
                }
                else if(selectedId==R.id.raCallme)
                {
                    selectnum="2";
                    radioButton = (RadioButton) findViewById(selectedId);
                    PrescriptionChoose=radioButton.getText().toString();
                }
                if(PrescriptionChoose.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please select Any option",Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent i = new Intent(getApplicationContext(), SelectAddressPrescriptionActivity.class);
                    i.putExtra("insert_id",insert_id);
                    i.putExtra("PrescriptionChoose",PrescriptionChoose);
                    i.putExtra("selectnum",selectnum);
                    startActivity(i);
                }
            }
        });

    }

    private void getUpdateInformation(final String prescriptiondetail,final String selectnum) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL+"product/getcart",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus=obj.getString("status");
                            if(strstatus.equals("false"))
                            {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("dfsasfd","profile error========="+error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("insert_id",insert_id);
                params.put("prescriptiondetail",prescriptiondetail);
                params.put("selectnum",selectnum);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }


}

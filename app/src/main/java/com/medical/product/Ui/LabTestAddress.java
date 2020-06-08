package com.medical.product.Ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.medical.product.R;
import com.medical.product.adapter.AdapterAddress;
import com.medical.product.helpingFile.GPSTracker;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.AddModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LabTestAddress extends AppCompatActivity {
    ArrayList<String> benName,benAge,benGender;
    String amountFinal,actualamount,savingamount;
    ImageView location;
    Keystore store;
    AdapterAddress adapterAddress;
    RecyclerView recyclerView;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference dbr;
    TextView total,addDialText,addDialClose;
    EditText addDialFlat,addDialAddr,addDialStat,addDialCity,addDialPin;
    Button addDialAdd;
    Button confirm;

    Dialog dialog;
    GPSTracker tracker;
    FloatingActionButton fab;
    String mFlat,mAddress,mState,mCity,mPincode;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        Bundle bundle = getIntent().getExtras();

        benName=bundle.getStringArrayList("benName");
        benAge=bundle.getStringArrayList("benAge");
        benGender=bundle.getStringArrayList("benGender");
        amountFinal = bundle.getString("totalamount");
        actualamount=bundle.getString("actualamount");
        savingamount=bundle.getString("savingamount");

        total = findViewById(R.id.tv_subTotal);
        total.setText("Total : "+amountFinal+" Rs/-");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.Recycle_AddressList);
        store = Keystore.getInstance(getApplicationContext());
        fab = findViewById(R.id.fab_addNewAddress);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(LabTestAddress.this);
                dialog.setContentView(R.layout.address_dialog);
                addDialClose=dialog.findViewById(R.id.close_dialg);
                location=dialog.findViewById(R.id.locationpickerForEditAddress);
                addDialClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                addDialFlat=dialog.findViewById(R.id.edtFlatno);
                addDialAddr=dialog.findViewById(R.id.edtAddress);
                addDialStat=dialog.findViewById(R.id.edtState);
                addDialCity=dialog.findViewById(R.id.edtCity);
                addDialPin=dialog.findViewById(R.id.edtPincode);
                addDialAdd=dialog.findViewById(R.id.btnSaveAddress);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tracker = new GPSTracker(LabTestAddress.this);
                        getAddressLocation(tracker.getLatitude(),tracker.getLongitude());

                    }
                });
                addDialAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(addDialFlat.getText().toString().trim().isEmpty() || addDialAdd.getText().toString().trim().isEmpty()
                                ||addDialStat.getText().toString().trim().isEmpty()|| addDialCity.getText().toString().trim().isEmpty()||
                                addDialPin.getText().toString().trim().isEmpty()){
                            Toast.makeText(LabTestAddress.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog = new ProgressDialog(LabTestAddress.this);
                            progressDialog.setMessage("Please wait, adding address");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            ReuseMethod.addAddress(dialog,getApplicationContext(),progressDialog,addDialFlat.getText().toString(),
                                    addDialAddr.getText().toString(),
                                    addDialStat.getText().toString(),
                                    addDialCity.getText().toString(),
                                    addDialPin.getText().toString());
                        }
                    }
                });
                dialog.show();
            }
        });
        confirm=findViewById(R.id.btn_Confirm);
        dbr=db.collection("+91"+store.get("phone")).document("Address").collection("1mlLab");
        setuprecyclerview();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(mFlat==null || mAddress==null || mState==null||mCity==null||mPincode==null||mFlat.isEmpty() || mAddress.isEmpty() ||mState.isEmpty() ||mCity.isEmpty()||mPincode.isEmpty())){
                    Intent intent = new Intent(LabTestAddress.this,LabTestReport.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flat",mFlat);
                    bundle1.putString("address",mAddress);
                    bundle1.putString("state",mState);
                    bundle1.putString("city",mCity);
                    bundle1.putString("pincode",mPincode);
                    bundle1.putStringArrayList("benName",benName);
                    bundle1.putStringArrayList("benAge",benAge);
                    bundle1.putStringArrayList("benGender",benGender);
                    bundle1.putString("totalamount",amountFinal);
                    bundle1.putString("actualamount",actualamount);
                    bundle1.putString("savingamount",savingamount);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }else{
                    Toast.makeText(LabTestAddress.this, "Please select valid address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getAddressLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "", lat = "", logt = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            for (int i = 0; i < addresses.size(); i++) {
                address = addresses.get(i).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(i).getLocality();
                String state = addresses.get(i).getAdminArea();
                String postalCode = addresses.get(i).getPostalCode();
                addDialAddr.setText(address);
                addDialCity.setText(city);
                addDialPin.setText(postalCode);
                addDialStat.setText(state);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setuprecyclerview(){
        Query query = dbr.orderBy("flat", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<AddModel> options = new FirestoreRecyclerOptions.Builder<AddModel>()
                .setQuery(query,AddModel.class).build();
        adapterAddress= new AdapterAddress(options, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterAddress);
        adapterAddress.setValues(new AdapterAddress.OnItemValue() {
            @Override
            public void onItemValue(String flat, String address, String state, String city, String pincode) {
                mFlat=flat;
                mAddress=address;
                mState=state;
                mCity=city;
                mPincode=pincode;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapterAddress.startListening();

    }
    @Override
    protected void onStart(){
        super.onStart();
        adapterAddress.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapterAddress.stopListening();
    }
    @Override
    protected void onPause(){
        super.onPause();
        adapterAddress.stopListening();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

}

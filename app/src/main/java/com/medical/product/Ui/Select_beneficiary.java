package com.medical.product.Ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.medical.product.adapter.AdapterBeneficiary;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.BenModel;
import com.razorpay.Checkout;

import java.util.ArrayList;

public class Select_beneficiary extends AppCompatActivity {
    String amunt="0";
    TextView subtotal;
    FloatingActionButton fab;
    ProgressDialog progressDialog;
    Dialog dialog;
    ArrayList<String> name,ageA,gender;
    String amountFinal="0";
    Keystore store;
    AdapterBeneficiary adapterBeneficiary;
    Button addBen,confirm;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference dbr;
    RecyclerView recyclerView;
    TextView closedialog;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    EditText firstName,lastName,age;
    String actualamount,savingamount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_beneficiary);
        Checkout.preload(getApplicationContext());
        final Bundle bundle = getIntent().getExtras();
        amunt = bundle.getString("totalamount");
        actualamount=bundle.getString("actualamount");
        savingamount=bundle.getString("savingamount");
        fab = findViewById(R.id.fab_addNewBen);
        confirm=findViewById(R.id.btn_Confirm);
        recyclerView = findViewById(R.id.Recycle_BenList);
        store = Keystore.getInstance(getApplicationContext());
        subtotal = findViewById(R.id.tv_subTotal);
        subtotal.setText("Total : " + amunt + "Rs/-");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Select_beneficiary.this);
                dialog.setContentView(R.layout.add_newben_dailog);
                closedialog=dialog.findViewById(R.id.close_addBendialg);
                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                firstName=dialog.findViewById(R.id.edtbenFName);
                lastName=dialog.findViewById(R.id.edtbenLName);
                age=dialog.findViewById(R.id.edt_age);
                radioSexGroup=dialog.findViewById(R.id.radGrpGender);
                addBen=dialog.findViewById(R.id.btn_addnewBen);
                addBen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(firstName.getText().toString().trim().isEmpty() || lastName.getText().toString().trim().isEmpty()
                        ||age.getText().toString().trim().isEmpty()||Integer.parseInt(age.getText().toString())<2){
                            Toast.makeText(Select_beneficiary.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog = new ProgressDialog(Select_beneficiary.this);
                            progressDialog.setMessage("Please wait, adding beneficiary");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            int selectedId = radioSexGroup.getCheckedRadioButtonId();
                            radioSexButton = dialog.findViewById(selectedId);
                            ReuseMethod.addBeneficiary(dialog,getApplicationContext(),progressDialog,firstName.getText().toString(),lastName.getText().toString(),radioSexButton.getText().toString(),age.getText().toString());

                        }
                    }
                });
                dialog.show();
            }
        });
        dbr=db.collection("+91"+store.get("phone")).document("Beneficiary").collection("1mlLab");
        setuprecyclerview();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountFinal.equals("0") || name.isEmpty()) {
                    Toast.makeText(Select_beneficiary.this, "Please select valid option", Toast.LENGTH_SHORT).show();
                }else{
            Intent intent = new Intent(Select_beneficiary.this,LabTestAddress.class);
            Bundle bundle1 = new Bundle();
            bundle1.putStringArrayList("benName",name);
                    bundle1.putStringArrayList("benAge",ageA);
                    bundle1.putStringArrayList("benGender",gender);
            bundle1.putString("totalamount",amountFinal);
            bundle1.putString("actualamount",actualamount);
            bundle1.putString("savingamount",savingamount);
            intent.putExtras(bundle1);
            startActivity(intent);
            }

            }
        });
    }
    private void setuprecyclerview(){
        Query query = dbr.orderBy("firstName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BenModel> options = new FirestoreRecyclerOptions.Builder<BenModel>()
                .setQuery(query,BenModel.class).build();
        adapterBeneficiary = new AdapterBeneficiary(options, this,Float.parseFloat(amunt));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterBeneficiary);
        adapterBeneficiary.setValues(new AdapterBeneficiary.OnItemValue() {
            @Override
            public void onItemValue(ArrayList<String> strings, String amount, ArrayList<String> age, ArrayList<String> mgender) {
                name=strings;
                amountFinal=amount;
                ageA=age;
                gender = mgender;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapterBeneficiary.startListening();

    }
    @Override
    protected void onStart(){
        super.onStart();
        adapterBeneficiary.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapterBeneficiary.stopListening();
    }
    @Override
    protected void onPause(){
        super.onPause();
        adapterBeneficiary.stopListening();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

}

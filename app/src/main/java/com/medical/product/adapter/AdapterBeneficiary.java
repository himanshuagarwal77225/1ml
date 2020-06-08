package com.medical.product.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.medical.product.R;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.BenModel;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterBeneficiary extends FirestoreRecyclerAdapter<BenModel, AdapterBeneficiary.BenHolder> {
    View v;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> ageA=new ArrayList<>();
    ArrayList<String> gender=new ArrayList<>();
    Context mContext;
    TextView remove,total;
    Dialog dialog;
    float amount=0;
    int counter=1;
    private OnItemValue listener;
    TextView closedialog;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    EditText firstName,lastName,age;
    Button addBen;
    ProgressDialog progressDialog;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterBeneficiary(@NonNull FirestoreRecyclerOptions<BenModel> options,Context context,float amount) {
        super(options);
        this.mContext=context;
        this.amount=amount;
    }

    @Override
    protected void onBindViewHolder(@NonNull final BenHolder benHolder, final int i, @NonNull final BenModel benModel) {
        remove = ((Activity)mContext).findViewById(R.id.centre);
        remove.setVisibility(View.GONE);
    benHolder.checkBox.setText(benModel.firstName);
    benHolder.m.setText(benModel.gender);
    benHolder.t.setText(benModel.age + " yrs");
    if(!name.contains(benModel.firstName+" "+benModel.lastName)) {
        name.add(benModel.firstName + " " + benModel.lastName);
        ageA.add(benModel.age);
        gender.add(benModel.gender);
    }
    benHolder.checkBox.setChecked(true);
        benHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!name.contains(benModel.firstName+" "+benModel.lastName)){
                        name.add(benModel.firstName+" "+benModel.lastName);
                        ageA.add(benModel.age);
                        gender.add(benModel.gender);

                    }
                }else{
                    name.remove(benModel.firstName+" "+benModel.lastName);
                    ageA.remove(benModel.age);
                    gender.remove(benModel.gender);
                }
                setValue();
            }
        });
        setValue();
        benHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(mContext);
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
                addBen.setText("Update");
                firstName.setText(benModel.firstName);
                lastName.setText(benModel.lastName);
                age.setText(benModel.age);
                addBen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(firstName.getText().toString().trim().isEmpty() || lastName.getText().toString().trim().isEmpty()
                                ||age.getText().toString().trim().isEmpty()||Integer.parseInt(age.getText().toString())<2){
                            Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        }else{
                            name.remove(benModel.firstName+" "+benModel.lastName);
                            ageA.remove(benModel.age);
                            gender.remove(benModel.gender);
                            progressDialog = new ProgressDialog(mContext);
                            progressDialog.setMessage("Please wait, updating beneficiary");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            int selectedId = radioSexGroup.getCheckedRadioButtonId();
                            radioSexButton = dialog.findViewById(selectedId);
                            getSnapshots().getSnapshot(i).getReference().delete();
                            ReuseMethod.addBeneficiary(dialog,mContext,progressDialog,firstName.getText().toString(),lastName.getText().toString(),radioSexButton.getText().toString(),age.getText().toString());
                                                    }
                    }
                });
                dialog.show();
            }
        });
        benHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReuseMethod.deleteBen(benModel.getFirstName() + " " + benModel.getLastName());
            }
        });
    }

    @NonNull
    @Override
    public BenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v =LayoutInflater.from(parent.getContext()).inflate(R.layout.select_ben_item, parent, false);
        return new BenHolder(v);
    }

    class BenHolder extends RecyclerView.ViewHolder{
         CheckBox checkBox;
         ImageView imageView;
         LinearLayout checkL;
         TextView m,t;
         ImageView delete;
        public BenHolder(@NonNull View itemView) {
            super(itemView);
            checkBox =  itemView.findViewById(R.id.chk_ben);
            checkL = itemView.findViewById(R.id.lin_checkben);
            imageView = itemView.findViewById(R.id.imgEditBen);
            m=itemView.findViewById(R.id.m);
            t=itemView.findViewById(R.id.t);
            delete = itemView.findViewById(R.id.imgDelBen);

        }
    }

private void setValue(){
        total =((Activity)mContext).findViewById(R.id.tv_subTotal);
        total.setText("Sub-Total : "+name.size()*amount+" Rs/-");
        listener.onItemValue(name, String.valueOf(name.size()*amount),ageA,gender);

}
    public interface OnItemValue {
        void onItemValue(ArrayList<String> strings,String amount,ArrayList<String> age,ArrayList<String> gender);
    }

    public void setValues(OnItemValue listener ){
        this.listener=listener;

    }

}
package com.medical.product.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.medical.product.R;
import com.medical.product.Ui.LabTestAddress;
import com.medical.product.helpingFile.GPSTracker;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.AddModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterAddress extends FirestoreRecyclerAdapter<AddModel, AdapterAddress.AddressHolder> {
    View v;
    Context mContext;
    TextView remove;
    Dialog dialog;
    ImageView location;
    GPSTracker tracker;
    ProgressDialog progressDialog;
    TextView addDialText,addDialClose;
    EditText addDialFlat,addDialAddr,addDialStat,addDialCity,addDialPin;
    Button addDialAdd;
    private OnItemValue listener;
    int mSelectedPosition=-1;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterAddress(@NonNull FirestoreRecyclerOptions<AddModel> options, Context context) {
        super(options);
        this.mContext=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull AddressHolder addressHolder, final int i, @NonNull final AddModel addModel) {
        remove = ((Activity)mContext).findViewById(R.id.remove);
        remove.setVisibility(View.GONE);
        addressHolder.flatadd.setText(addModel.getFlat()+", "+addModel.getAddress());
        addressHolder.statcity.setText(addModel.getCity()+", "+addModel.getState());
        addressHolder.pin.setText(addModel.getPincode());
        addressHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.address_dialog);
                addDialText=dialog.findViewById(R.id.dialgheader);
                addDialText.setText("Edit Address");
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
                addDialAdd.setText("Update");
                addDialFlat.setText(addModel.getFlat());
                addDialAddr.setText(addModel.getAddress());
                addDialStat.setText(addModel.getState());
                addDialCity.setText(addModel.getCity());
                addDialPin.setText(addModel.getPincode());
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tracker = new GPSTracker(mContext);
                        getAddressLocation(tracker.getLatitude(),tracker.getLongitude());

                    }
                });
                addDialAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(addDialFlat.getText().toString().trim().isEmpty() || addDialAdd.getText().toString().trim().isEmpty()
                                ||addDialStat.getText().toString().trim().isEmpty()|| addDialCity.getText().toString().trim().isEmpty()||
                                addDialPin.getText().toString().trim().isEmpty()){
                            Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog = new ProgressDialog(mContext);
                            progressDialog.setMessage("Please wait, adding address");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            getSnapshots().getSnapshot(i).getReference().delete();
                            ReuseMethod.addAddress(dialog,mContext,progressDialog,addDialFlat.getText().toString(),
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
        addressHolder.choice.setChecked(mSelectedPosition == i);
        addressHolder.choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    setValue(addModel.getFlat(),addModel.getAddress(),addModel.getState(),addModel.getCity(),addModel.getPincode());
                }
            }
        });
        addressHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReuseMethod.deleteAdd(addModel.getPincode());
            }
        });
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_address_item, parent, false);
        return new AddressHolder(v);
    }

    class AddressHolder extends RecyclerView.ViewHolder{
        RadioButton choice;
        TextView flatadd,statcity,pin;
        Button edit;
        Button delete;
        public AddressHolder(@NonNull View itemView) {
            super(itemView);
            choice = itemView.findViewById(R.id.rb_selectAddRadio);
            flatadd = itemView.findViewById(R.id.tv_address);
            statcity = itemView.findViewById(R.id.tv_state_city);
            pin = itemView.findViewById(R.id.tv_pincode);
            edit = itemView.findViewById(R.id.btn_editaddress);
            delete=itemView.findViewById(R.id.btn_deladdress);
            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
    public void getAddressLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
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
    public interface OnItemValue {
        void onItemValue(String flat, String address,String state,String city,String pincode);
    }

    public void setValues(OnItemValue listener ){
        this.listener=listener;

    }
    private void setValue(String flat, String address,String state,String city,String pincode){
        listener.onItemValue(flat,address,state,city,pincode);

    }
}

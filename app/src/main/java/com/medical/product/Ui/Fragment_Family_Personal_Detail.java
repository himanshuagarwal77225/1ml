package com.medical.product.Ui;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleyMultipartRequest;
import com.medical.product.helpingFile.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link } subclass.
 */
public class Fragment_Family_Personal_Detail extends Fragment {
    EditText edtUsername,edtUseremail,edtUserphone,edtUserWeight,edtUserEContactnumber,edtUserHeight;
    TextView txtDateofbirth;
    Button btnSubmit;
    Spinner spinnergender,spinnerBloodGroup,spinnerMarriageStatus,spinnerrelation;
    final Calendar myCalendar = Calendar.getInstance();

    private Keystore store;
    int GET_TEXT_REQUEST_CODE=99;

    Context context=getActivity();
    View rootview;

    String iscreated="";
    String strmamberid="";
    ImageView imgeditimage;
    CircleImageView CimgivThumb;
    Bitmap bitmapmain=null;
    ProgressDialog dialog;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public Fragment_Family_Personal_Detail() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootview= inflater.inflate(R.layout.fragment_fragment__family__personal__detail, container, false);
        dialog = new ProgressDialog(getActivity());
        ComponentInitialization(rootview);
        store = Keystore.getInstance(getActivity());

         strmamberid=  ((Family_Mamber_Activity) getActivity()).mamberid;



         if(!(strmamberid.equals("") || strmamberid.equals(null) || strmamberid.equals("null")))
         {
             GetMamberData();
             btnSubmit.setText("Update");
         }
         else
         {
             btnSubmit.setText("Create");
         }


        return rootview;

    }

    private void getUserData() {
        store = Keystore.getInstance(getActivity());
        txtDateofbirth.setText(store.get("dob"));
        edtUsername.setText(store.get("name"));
        edtUseremail.setText(store.get("email"));
        edtUserphone.setText(store.get("phone"));
        edtUserHeight.setText(store.get("height"));
        edtUserWeight.setText(store.get("weight"));
        edtUserEContactnumber.setText(store.get("em_co_nu"));
    }

    private void ComponentInitialization(View rootview) {
        btnSubmit=(Button) rootview.findViewById(R.id.btnSubmit);
        txtDateofbirth=(TextView) rootview.findViewById(R.id.txtDateofbirth);

        spinnergender=(Spinner)rootview.findViewById(R.id.spinnergender);
        spinnerBloodGroup=(Spinner)rootview.findViewById(R.id.spinnerBloodGroup);
        spinnerMarriageStatus=(Spinner)rootview.findViewById(R.id.spinnerMarriageStatus);
        spinnerrelation=(Spinner)rootview.findViewById(R.id.spinnerrelation);
        spinnerBloodGroup.setPrompt("Blood Group...");
        spinnerBloodGroup.setPopupBackgroundResource(R.drawable.spinner_popup_background);

        imgeditimage=(ImageView) rootview.findViewById(R.id.imgeditimage);
        CimgivThumb=(CircleImageView) rootview.findViewById(R.id.CimgivThumb);

        spinnerMarriageStatus.setPrompt("Marriage...");


        edtUsername=(EditText)rootview.findViewById(R.id.edtUsername);

        edtUseremail=(EditText)rootview.findViewById(R.id.edtUseremail);

        edtUserphone=(EditText)rootview.findViewById(R.id.edtUserphone);


        edtUserHeight=(EditText) rootview.findViewById(R.id.edtUserHeight);

        edtUserWeight=(EditText)rootview.findViewById(R.id.edtUserWeight);

        edtUserEContactnumber=(EditText)rootview.findViewById(R.id.edtUserEContactnumber);

        txtDateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtUsername.getText().equals(""))
                {
                    Toast.makeText(getActivity(),"Please Enter mambername",Toast.LENGTH_SHORT).show();
                }
                else if(edtUserphone.getText().equals(""))
                {
                    Toast.makeText(getActivity(),"Please Enter phone",Toast.LENGTH_SHORT).show();
                }
                else if(txtDateofbirth.getText().equals(""))
                {
                    Toast.makeText(getActivity(),"Please Enter mambername",Toast.LENGTH_SHORT).show();
                }
                else if(spinnerrelation.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getActivity(),"Please select Relation",Toast.LENGTH_SHORT).show();
                }
                else {

                    if(bitmapmain!=null)
                    {
                        uploadBitmap();
                    }
                    else {
                        UpdateUserData();
                    }


                   // UpdateUserData();
                }



            }
        });

        edtUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String sf = s.toString();
                if (!sf.equals(s.toString().toUpperCase())) {
                    edtUsername.setText(s.toString().toUpperCase());
                    edtUsername.setSelection(edtUsername.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        imgeditimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.prompt, null);

                final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();

                Button btnCamera = (Button) promptView.findViewById(R.id.btnCamera);

                Button btnGallery = (Button) promptView.findViewById(R.id.btnGallery);

                btnCamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 33);
                        // btnAdd1 has been clicked
                        alertD.dismiss();
                    }
                });

                btnGallery.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 100);
                        alertD.dismiss();
                    }
                });

                alertD.setView(promptView);

                alertD.show();
            }
        });


    }

    private void selectImageOption(View v) {

    }


    private void updateLabel() {
        String myFormat = "LLLL dd yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDateofbirth.setText(sdf.format(myCalendar.getTime()));
    }

    private void UpdateUserData() {
        dialog.setMessage("create");
        dialog.setCancelable(false);
        dialog.show();

        String uriid="";
        if(strmamberid.equals("") || strmamberid.equals(null))
        {
            uriid=ApiFileuri.ROOT_HTTP_URL+"add-family";
        }
        else
        {
            uriid=ApiFileuri.ROOT_HTTP_URL+"update-family";

        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, uriid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);


                            String strstatus = obj.getString("status");

                            if(strstatus.equals("false"))
                            {
                                dialog.dismiss();
                                Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();

                            }
                            else{

                                dialog.dismiss();
                                CimgivThumb.setImageBitmap(bitmapmain);

                                bitmapmain=null;
                                if(strmamberid.equals("") || strmamberid.equals(null))
                                {
                                    strmamberid=obj.getString("id");
                                    strmamberid=obj.getString("id");
                                    ((Family_Mamber_Activity)getActivity()).setMyString(obj.getString("id"));
                                }

                                Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("member_name", edtUsername.getText().toString());
                params.put("contact_number", edtUserphone.getText().toString());
                params.put("email_id", edtUseremail.getText().toString());
                params.put("date_of_birth", txtDateofbirth.getText().toString());
                params.put("height", edtUserHeight.getText().toString());
                params.put("weight", edtUserWeight.getText().toString());
                params.put("emergency_contact", edtUserEContactnumber.getText().toString());
                params.put("realtion", spinnerrelation.getSelectedItem().toString());
               int bloodgrouppos= spinnerBloodGroup.getSelectedItemPosition();
                int maritpos= spinnerMarriageStatus.getSelectedItemPosition();
                int gendpos= spinnergender.getSelectedItemPosition();

               if(bloodgrouppos!=0)
               {
                   params.put("blood_group", spinnerBloodGroup.getSelectedItem().toString());
               }
                if(maritpos!=0)
                {
                    params.put("marital_status", spinnerMarriageStatus.getSelectedItem().toString());
                }

                if(gendpos!=0)
                {
                    params.put("gender", spinnergender.getSelectedItem().toString());
                }

                if(strmamberid.equals("") || strmamberid.equals(null))
                {
                    params.put("user_id", store.get("id"));
                }
                else
                {
                    params.put("id",  strmamberid);
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private void uploadBitmap() {
        dialog.setMessage("create");
        dialog.setCancelable(false);
        dialog.show();

        String uriid="";
        if(strmamberid.equals("") || strmamberid.equals(null))
        {
            uriid=ApiFileuri.ROOT_HTTP_URL+"add-family";
        }
        else
        {
            uriid=ApiFileuri.ROOT_HTTP_URL+"update-family";

        }

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uriid,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {


                            dialog.dismiss();

                            JSONObject obj = new JSONObject(new String(response.data));
                            CimgivThumb.setImageBitmap(bitmapmain);
                            bitmapmain=null;
                            if(strmamberid.equals("") || strmamberid.equals(null))
                            {
                                strmamberid=obj.getString("id");
                                ((Family_Mamber_Activity)getActivity()).setMyString(obj.getString("id"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {



            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("member_name", edtUsername.getText().toString());
                params.put("contact_number", edtUserphone.getText().toString());
                params.put("email_id", edtUseremail.getText().toString());
                params.put("date_of_birth", txtDateofbirth.getText().toString());
                params.put("height", edtUserHeight.getText().toString());
                params.put("weight", edtUserWeight.getText().toString());
                params.put("emergency_contact", edtUserEContactnumber.getText().toString());
                int bloodgrouppos= spinnerBloodGroup.getSelectedItemPosition();
                int maritpos= spinnerMarriageStatus.getSelectedItemPosition();
                int gendpos= spinnergender.getSelectedItemPosition();

                if(bloodgrouppos!=0)
                {
                    params.put("blood_group", spinnerBloodGroup.getSelectedItem().toString());
                }
                if(maritpos!=0)
                {
                    params.put("marital_status", spinnerMarriageStatus.getSelectedItem().toString());
                }

                if(gendpos!=0)
                {
                    params.put("gender", spinnergender.getSelectedItem().toString());
                }

                if(strmamberid.equals("") || strmamberid.equals(null))
                {
                    params.put("user_id", store.get("id"));
                }
                else
                {
                    params.put("id",  strmamberid);
                }
                return params;
            }

           @Override
            protected Map<String, DataPart> getByteData() {
               Map<String, DataPart> params = new HashMap<>();
                if(!bitmapmain.equals(null))
                {
                    long imagename = System.currentTimeMillis();
                    params.put("userfile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapmain)));
                }
               return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void GetMamberData() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL+"get-one-family",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if(strstatus.equals("false"))
                            {
                                Toast.makeText(getActivity(),obj.getString("user_data"),Toast.LENGTH_SHORT).show();
                            }
                            else{
                                obj.getJSONObject("message").getString("id");
                                obj.getJSONObject("message").getString("contact_number");
                                obj.getJSONObject("message").getString("email_id");
                                obj.getJSONObject("message").getString("gender");
                                obj.getJSONObject("message").getString("realtion");
                                obj.getJSONObject("message").getString("date_of_birth");
                                obj.getJSONObject("message").getString("blood_group");
                                obj.getJSONObject("message").getString("marital_status");
                                obj.getJSONObject("message").getString("height");
                                obj.getJSONObject("message").getString("weight");
                                obj.getJSONObject("message").getString("emergency_contact");
                                obj.getJSONObject("message").getString("allergies");
                                obj.getJSONObject("message").getString("current_medications");
                                obj.getJSONObject("message").getString("past_medications");
                                obj.getJSONObject("message").getString("chronic_diseases");
                                obj.getJSONObject("message").getString("injuries");
                                obj.getJSONObject("message").getString("surgeries");
                                obj.getJSONObject("message").getString("smoking_habits");
                                obj.getJSONObject("message").getString("alcohol_consumption");
                                obj.getJSONObject("message").getString("activity_level");
                                obj.getJSONObject("message").getString("food_preference");
                                obj.getJSONObject("message").getString("occupation");

                                edtUsername.setText(obj.getJSONObject("message").getString("member_name"));
                                edtUseremail.setText(obj.getJSONObject("message").getString("email_id"));
                                edtUserphone.setText(obj.getJSONObject("message").getString("contact_number"));
                                edtUserWeight.setText(obj.getJSONObject("message").getString("weight"));
                                edtUserEContactnumber.setText(obj.getJSONObject("message").getString("emergency_contact"));
                                edtUserHeight.setText(obj.getJSONObject("message").getString("height"));
                                txtDateofbirth.setText(obj.getJSONObject("message").getString("date_of_birth"));

                                if( !(obj.getJSONObject("message").getString("member_image").equals("") || obj.getJSONObject("message").getString("member_image").equals("")))
                                {
                                    Glide.with(getActivity()).load(ApiFileuri.ROOT_URL_USER_IMAGE+obj.getJSONObject("message").getString("member_image")).into(CimgivThumb);
                                }

                                final String[] bloodgrouparr = getResources().getStringArray(R.array.bloodgroup_array);
                                if(obj.getJSONObject("message").getString("blood_group")!=null) {
                                    for (int x = 0; x < bloodgrouparr.length; x++) {
                                        if (obj.getJSONObject("message").getString("blood_group").equals(bloodgrouparr[x])) {
                                            spinnerBloodGroup.setSelection(x);
                                        }
                                    }
                                }

                                final String[] marriagearr = getResources().getStringArray(R.array.marriagestatus_array);
                                if( obj.getJSONObject("message").getString("marital_status")!=null) {
                                    for (int x = 0; x < marriagearr.length; x++) {
                                        if ( obj.getJSONObject("message").getString("marital_status").equals(marriagearr[x])) {
                                            spinnerMarriageStatus.setSelection(x);
                                        }
                                    }
                                }


                                final String[] genderarr = getResources().getStringArray(R.array.gender_array);
                                if(obj.getJSONObject("message").getString("gender")!=null) {
                                    for (int x = 0; x < genderarr.length; x++) {
                                        if (obj.getJSONObject("message").getString("gender").equals(genderarr[x])) {
                                            spinnergender.setSelection(x);
                                        }
                                    }
                                }

                                final String[] relations = getResources().getStringArray(R.array.relationon);
                                if(obj.getJSONObject("message").getString("realtion")!=null) {
                                    for (int x = 0; x < relations.length; x++) {
                                        if (obj.getJSONObject("message").getString("realtion").equals(relations[x])) {
                                            spinnerrelation.setSelection(x);
                                        }
                                    }
                                }





                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id",strmamberid);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            bitmapmain=null;
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmapmain = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                //displaying selected image to imageview
              //  imgeditimage.setImageBitmap(bitmapmain);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 33) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getActivity(), photo);

            Toast.makeText(getActivity(), "Image"+tempUri,
                    Toast.LENGTH_LONG).show();
            File finalFile = new File(getRealPathFromURI(tempUri));

            try {
                bitmapmain = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(finalFile));

             //   imgeditimage.setImageBitmap(bitmapmain);


            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor =getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }








}

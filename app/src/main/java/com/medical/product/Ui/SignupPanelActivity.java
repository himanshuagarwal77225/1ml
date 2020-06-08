package com.medical.product.Ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.ConnectDetector;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.SharedPrefManager;
import com.medical.product.models.UserRegistrationResponseModel;
import com.medical.product.rest.ApiClient;
import com.medical.product.rest.ApiInterface;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class SignupPanelActivity extends AppCompatActivity {
    EditText edtPhoneNumber, edtReferralcode, edtEmail, edtname, edtphoneotp;
    Map<String, Object> user;
    FirebaseFirestore db;
    String phone, verified;
    Dialog nesteddialog;
    DocumentReference documentReference;
    FirebaseAuth firebaseAuth;
    String verifyCode;
    ProgressDialog dialog;
    PhoneAuthCredential credential;
    CheckedTextView chkTxtRefralcode;
    Button btnRegister;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    ConnectDetector cd;
    Handler handler;
    // final Calendar myCalendar = Calendar.getInstance();
    Boolean isinternetpresent;
    Dialog dialog1;
    TextView txtLogin;
    private String mVerificationId;

    private Keystore store;
    String strphone;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_test);
        hideStatusbar(this);
        db = FirebaseFirestore.getInstance();
        store = Keystore.getInstance(getApplicationContext());
        phone = store.get("phone");
        verified = store.get("verified");
        ComponentInitialization();
        ReuseMethod.SharedPrefranceGetCartValueClear(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler(getApplicationContext().getMainLooper());
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                showprogress();
                btnRegister.setText("Send");
                btnRegister.setEnabled(true);
                btnRegister.setClickable(true);
                Toast.makeText(SignupPanelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showprogress();
                        Utlity.dismiss_dilog(SignupPanelActivity.this, nesteddialog);
                        Toast.makeText(getApplicationContext(), "Verification code has been sent", Toast.LENGTH_SHORT).show();
                        dialog1 = new Dialog(SignupPanelActivity.this);
                        dialog1.setContentView(R.layout.verification_dialog);
                        dialog1.setCancelable(false);
                        final TextView textView = dialog1.findViewById(R.id.dialog_text);
                        textView.setText("We sent a 6 digit verification code to " + strphone + ". Enter it below.");
                        final TextView verifyButton = dialog1.findViewById(R.id.btn_verify);
                        TextView resendButton = dialog1.findViewById(R.id.btn_resend);
                        verifyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                EditText verificationCode = dialog1.findViewById(R.id.code);
                                verifyCode = verificationCode.getText().toString();
                                verifyButton.setClickable(false);
                                verifyButton.setEnabled(false);
                                if (!verifyCode.trim().isEmpty()) {

                                    credential = PhoneAuthProvider.getCredential(mVerificationId, verifyCode);
                                    signInWithPhoneAuthCredential(credential);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });
                        resendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                btnRegister.setText("LOGIN");
                                btnRegister.setEnabled(true);
                                btnRegister.setClickable(true);
                            }
                        });
                        dialog1.show();
                    }
                });
            }
        };
    }

    private void ComponentInitialization() {
        //  txtDateofbirth=(TextView) findViewById(R.id.txtDateofbirth);
        edtEmail = (EditText) findViewById(R.id.edtemail);
        edtname = (EditText) findViewById(R.id.edtname);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        // spinnergender=(Spinner)findViewById(R.id.spinnergender);
        //   spinnergender.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber1);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        chkTxtRefralcode = (CheckedTextView) findViewById(R.id.chkTxtRefralcode);
        edtReferralcode = (EditText) findViewById(R.id.edtReferralcode);
        //   spinnergender.setPrompt("Select gender...");
     /*   SpinnerCustomAdapter customAdapter=new SpinnerCustomAdapter(getApplicationContext(),genderimage,gendername);
        spinnergender.setAdapter(customAdapter);*/
//        btnRegister.setBackgroundDrawable( new DrawableGradient(new int[] { 0xffffffff, 0xffffffff, 0xffffffff }, 0).SetTransparency(20));
        if (!phone.equals("")) {
            edtPhoneNumber.setText(phone);
            edtPhoneNumber.setClickable(false);
            edtPhoneNumber.setFocusable(false);
        }
    }

    public void clickFunction(View view) {
        switch (view.getId()) {
           /* case R.id.txtDateofbirth:
                new DatePickerDialog(SignupPanelActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;*/
            case R.id.btnRegister:
                showprogress();
                String strphone = edtPhoneNumber.getText().toString();
                String stremail = edtEmail.getText().toString();
                String strname = edtname.getText().toString();
                Boolean mail = isValidMail(stremail);
                if (strphone.length() < 10 || strphone.equals("")) {
                    showprogress();
                    Toast.makeText(getApplicationContext(), "Incorrect Phone Number", Toast.LENGTH_SHORT).show();
                } else if (stremail.equals("")) {
                    showprogress();
                    Toast.makeText(getApplicationContext(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                } else if (strname.equals("")) {
                    showprogress();
                    Toast.makeText(getApplicationContext(), "Please Enter name", Toast.LENGTH_SHORT).show();
                } else if (!mail.equals(true)) {
                    showprogress();
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtReferralcode.getText().toString().isEmpty()) {
                        edtReferralcode.setText("NO CODE");
                    }
                    if (verified.equals("Yes")) {
                        showprogress();
                        CreateAccountDatabase(edtEmail.getText().toString(), "+91" + edtPhoneNumber.getText().toString(), edtname.getText().toString(), edtReferralcode.getText().toString());
                    } else {
                        login();
                    }
                }
                break;
            case R.id.chkTxtRefralcode:
                if (chkTxtRefralcode.isChecked()) {
                    chkTxtRefralcode.setChecked(false);
                    edtReferralcode.setVisibility(View.VISIBLE);
                } else {
                    chkTxtRefralcode.setChecked(true);
                    edtReferralcode.setVisibility(View.GONE);
                }
                break;
            case R.id.txtLogin:
                finish();
                break;


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        check = m.matches();
        return check;
    }

    private void CreateAccountDatabase(final String stremail, final String strphone, final String name, final String referal) {
        store.put("verified", "Yes");
        store.put("phone", edtPhoneNumber.getText().toString());
        store.put("name", name.toUpperCase());
        store.put("email", stremail);
        store.put("token", SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken());
        store.put("id", "1ML" + edtPhoneNumber.getText().toString() + "name");
        user = new HashMap<>();
        user.put("name", name);
        user.put("email", stremail);
        user.put("phone", strphone);
        user.put("mac", ReuseMethod.MacAddress(getApplicationContext()));
        user.put("token", SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken());
        user.put("referal", referal);
        user.put("date", DateFormat.getDateTimeInstance().format(new Date()));
        user.put("id", "1ML" + edtPhoneNumber.getText().toString() + "email");
        db.collection(strphone).document("ProfileInformation").set(user);
        getFCMToken();
       /* Intent intent = new Intent(SignupPanelActivity.this,Dashbord.class);
        startActivity(intent);
        finish();*/

    }

    public void login() {
        cd = new ConnectDetector(getApplicationContext());
        isinternetpresent = cd.isConnectToInternet();
        if (isinternetpresent) {
            strphone = "+91" + edtPhoneNumber.getText().toString();
            if (strphone.equals("") || strphone.length() < 10) {
                showprogress();
                Toast.makeText(getApplicationContext(), "Incorrect Phone Number", Toast.LENGTH_SHORT).show();
            } else {
                btnRegister.setText("Wait...");
                btnRegister.setEnabled(false);
                btnRegister.setClickable(false);
                Toast.makeText(getApplicationContext(), "Wait, login processing", Toast.LENGTH_SHORT).show();
                sendCode(strphone);
            }
        } else {
            showprogress();
            Toast.makeText(getApplicationContext(), "Internet Not Present", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks

        );

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        dialog1.dismiss();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db = FirebaseFirestore.getInstance();
                            documentReference = db.collection("+91" + edtPhoneNumber.getText().toString()).document("ProfileInformation");
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        btnRegister.setVisibility(View.VISIBLE);
                                        btnRegister.setClickable(true);
                                        btnRegister.setEnabled(true);
                                        btnRegister.setText("Register");
                                        Toast.makeText(SignupPanelActivity.this, "User already exists, Please login", Toast.LENGTH_SHORT).show();

                                    } else {
                                        CreateAccountDatabase(edtEmail.getText().toString(), "+91" + edtPhoneNumber.getText().toString(), edtname.getText().toString().toUpperCase(), edtReferralcode.getText().toString());
                                    }
                                }
                            });

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                btnRegister.setVisibility(View.VISIBLE);
                                btnRegister.setClickable(true);
                                btnRegister.setEnabled(true);
                                btnRegister.setText("Register");
                                Toast.makeText(SignupPanelActivity.this, "Credential error !! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void showprogress() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait, Fetching response");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else if (dialog.isShowing()) {
            dialog.dismiss();
        } else if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

    }

    private void getFCMToken() {
        showprogress();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Log.w("SignupPanelActivity", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    OneMLUserSpace.getInstance(SignupPanelActivity.this).setFCMToken(token);
                    callRegisterAPI();
                });
    }

    private void callRegisterAPI() {
        ApiClient.changeApiBaseUrl(ApiFileuri.ROOT_HTTP_URL);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class);
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("name", edtname.getText().toString());
        fields.put("phone", edtPhoneNumber.getText().toString());
        fields.put("email", edtEmail.getText().toString());
        if (HelperFunction.checkStringEmpty(edtReferralcode.getText().toString())) {
            fields.put("ref_code", edtReferralcode.getText().toString());
        }
        fields.put("token", OneMLUserSpace.getInstance(SignupPanelActivity.this).getFCMToken());
        Call<UserRegistrationResponseModel> liveStatsAPICall = apiService.postRegisterUser(fields);
        //ProgressDialog.showDialog(LoginActivity.this);
        liveStatsAPICall.enqueue(new Callback<UserRegistrationResponseModel>() {
            @Override
            public void onResponse(Call<UserRegistrationResponseModel> call, Response<UserRegistrationResponseModel> response) {
                showprogress();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            if (response.body().getUserData() != null && response.body().getUserData().size() > 0 && response.body().getUserData().get(0) != null) {
                                OneMLUserSpace.getInstance(SignupPanelActivity.this).setUserProfile(response.body().getUserData().get(0));
                                OneMLUserSpace.getInstance(SignupPanelActivity.this).setLogInStatus(1);
                                if (HelperFunction.checkStringEmpty(response.body().getUserData().get(0).getUserId())) {
                                    OneMLUserSpace.getInstance(SignupPanelActivity.this).set1MLUserID(response.body().getUserData().get(0).getUserId());
                                }
                                if (HelperFunction.checkStringEmpty(response.body().getUserData().get(0).getId())) {
                                    OneMLUserSpace.getInstance(SignupPanelActivity.this).setUserID(response.body().getUserData().get(0).getId());
                                }
                                if (HelperFunction.checkStringEmpty(response.body().getUserData().get(0).getAuthKey())) {
                                    OneMLUserSpace.getInstance(SignupPanelActivity.this).setToken(response.body().getUserData().get(0).getAuthKey());
                                }
                                redirectToDashBoard();
                            }
                        } else {
                            if (HelperFunction.checkStringEmpty(response.body().getMessage())) {
                                Toast.makeText(SignupPanelActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupPanelActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Toast.makeText(SignupPanelActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(SignupPanelActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationResponseModel> call, Throwable t) {
                showprogress();
                Log.e("SignupPanelActivity", t.getMessage(), t);
                Toast.makeText(SignupPanelActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToDashBoard() {
        Intent intent = new Intent(SignupPanelActivity.this, Dashbord.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

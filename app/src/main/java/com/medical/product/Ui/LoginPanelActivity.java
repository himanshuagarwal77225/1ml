package com.medical.product.Ui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import com.medical.product.models.UserRegistrationResponseModel;
import com.medical.product.rest.ApiClient;
import com.medical.product.rest.ApiInterface;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginPanelActivity extends AppCompatActivity {
    Button sendButton;
    LinearLayout layOtp, layLogin;
    String strphone;
    //   FirebaseAuth mAuth;
    TextView txtCreateAccount;
    private Keystore store;
    EditText edtphoneemail;
    Boolean clicked = false;
    DocumentReference documentReference;
    private String mVerificationId;
    Context context = LoginPanelActivity.this;
    EditText edtphoneotp;
    FirebaseFirestore db;
    PhoneAuthCredential credential;
    FirebaseAuth firebaseAuth;
    TextInputLayout layoutTextInput;
    boolean valtype;
    /*  private String mVerificationId;
      private PhoneAuthProvider.ForceResendingToken mResendToken;*/
    Boolean isinternetpresent;
    ConnectDetector cd;
    // sms permission
    Dialog nesteddialog;
    String verifyCode;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    ProgressDialog dialog;
    Dialog dialog1;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Handler handler;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler(getApplicationContext().getMainLooper());
        componentInitialization();

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                showprogress();
                sendButton.setText("Send");
                sendButton.setEnabled(true);
                sendButton.setClickable(true);
                Toast.makeText(LoginPanelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showprogress();
                        Utlity.dismiss_dilog(LoginPanelActivity.this, nesteddialog);
                        Toast.makeText(getApplicationContext(), "Verification code has been sent", Toast.LENGTH_SHORT).show();
                        dialog1 = new Dialog(LoginPanelActivity.this);
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
                                sendButton.setText("LOGIN");
                                sendButton.setEnabled(true);
                                sendButton.setClickable(true);
                            }
                        });
                        dialog1.show();
                    }
                });
            }
        };
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprogress();
                login();

            }
        });
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store = Keystore.getInstance(getApplicationContext());
                store.put("phone", "");
                store.put("verified", "No");
                Intent intent = new Intent(LoginPanelActivity.this, SignupPanelActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if the intent contains an AppInvite and then process the referral information.
        Intent intent = getIntent();

    }


    private void componentInitialization() {
        edtphoneemail = (EditText) findViewById(R.id.edtphoneemail);

        txtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        sendButton = (Button) findViewById(R.id.btnLogin);
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

    public void login() {
        cd = new ConnectDetector(getApplicationContext());
        isinternetpresent = cd.isConnectToInternet();
        if (isinternetpresent) {
            strphone = "+91" + edtphoneemail.getText().toString();
            if (strphone.length() < 10 || strphone.equals("")) {
                showprogress();
                Toast.makeText(getApplicationContext(), "Incorrect Phone Number", Toast.LENGTH_SHORT).show();
            } else {
                sendButton.setText("Wait...");
                sendButton.setEnabled(false);
                sendButton.setClickable(false);
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
                            database(strphone);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                sendButton.setVisibility(View.VISIBLE);
                                sendButton.setClickable(true);
                                sendButton.setEnabled(true);
                                sendButton.setText("Login");
                                Toast.makeText(LoginPanelActivity.this, "Credential error !! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void database(final String phone) {
        store = Keystore.getInstance(getApplicationContext());
        store.put("phone", edtphoneemail.getText().toString());
        store.put("verified", "Yes");
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection(phone).document("ProfileInformation");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    store.put("name", documentSnapshot.getString("name"));
                    store.put("email", documentSnapshot.getString("email"));
                    store.put("id", documentSnapshot.getString("id"));
                    /*Intent intent = new Intent(LoginPanelActivity.this, Dashbord.class);
                    startActivity(intent);
                    finish();*/
                    getFCMToken();

                } else {
                    getFCMToken();
                   // checkdata(phone);
                }
            }
        });

    }

    private void checkdata(final String phoneNumber) {
        Intent intent = new Intent(LoginPanelActivity.this, SignupPanelActivity.class);
        intent.putExtra("phone", phoneNumber);
        startActivity(intent);
        finish();
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

    private void callLoginAPI() {
        ApiClient.changeApiBaseUrl(ApiFileuri.ROOT_HTTP_URL);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class);
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("login_for", edtphoneemail.getText().toString());
        fields.put("token", OneMLUserSpace.getInstance(LoginPanelActivity.this).getFCMToken());
        Call<UserRegistrationResponseModel> liveStatsAPICall = apiService.postLoginUser(fields);
        showprogress();
        liveStatsAPICall.enqueue(new Callback<UserRegistrationResponseModel>() {
            @Override
            public void onResponse(Call<UserRegistrationResponseModel> call, Response<UserRegistrationResponseModel> response) {
                showprogress();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getUserData() != null && response.body().getUserData().size() > 0 && response.body().getUserData().get(0) != null) {
                            OneMLUserSpace.getInstance(LoginPanelActivity.this).setUserProfile(response.body().getUserData().get(0));
                            OneMLUserSpace.getInstance(LoginPanelActivity.this).setLogInStatus(1);
                            if (HelperFunction.checkStringEmpty(response.body().getUserData().get(0).getUserId())) {
                                OneMLUserSpace.getInstance(LoginPanelActivity.this).set1MLUserID(response.body().getUserData().get(0).getUserId());
                            }
                            if (HelperFunction.checkStringEmpty(response.body().getUserData().get(0).getId())) {
                                OneMLUserSpace.getInstance(LoginPanelActivity.this).setUserID(response.body().getUserData().get(0).getId());
                            }
                            if (HelperFunction.checkStringEmpty(response.body().getUserData().get(0).getAuthKey())) {
                                OneMLUserSpace.getInstance(LoginPanelActivity.this).setToken(response.body().getUserData().get(0).getAuthKey());
                            }
                            redirectToDashBoard();
                        } else {
                            checkdata(strphone);
                        }

                    } else {
                        Toast.makeText(LoginPanelActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginPanelActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationResponseModel> call, Throwable t) {
                showprogress();
                Log.e("TAG", t.getMessage(), t);
            }
        });
    }

    private void redirectToDashBoard() {
        Intent intent = new Intent(LoginPanelActivity.this, Dashbord.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void getFCMToken() {
        showprogress();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Log.w("LoginPanelActivity", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    OneMLUserSpace.getInstance(LoginPanelActivity.this).setFCMToken(token);
                    callLoginAPI();
                });
    }
}

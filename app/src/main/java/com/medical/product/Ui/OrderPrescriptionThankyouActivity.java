package com.medical.product.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.helpingFile.Keystore;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class OrderPrescriptionThankyouActivity extends Activity {

    private Keystore store;
    Button btngotoHome;
    TextView txtOrderid;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    String orderid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_prescription_order_thankyou);
        UploadPrescriptionActivity.is_next=false;
        txtOrderid = (TextView) findViewById(R.id.txtOrderid);
        btngotoHome = (Button) findViewById(R.id.btngotoHome);

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");

        if (!(orderid.equals("") || orderid.equals(null))) {
            txtOrderid.setText(orderid.toString());
        }


    }


    public void clickFunction(View view) {
        switch (view.getId()) {

            case R.id.btngotoHome:

                Intent i1 = new Intent(OrderPrescriptionThankyouActivity.this, Dashbord.class);
// set the new task and clear flags
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
                break;

        }
    }


}

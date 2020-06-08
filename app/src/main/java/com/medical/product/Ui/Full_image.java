package com.medical.product.Ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.medical.product.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Full_image extends AppCompatActivity {

    ImageView imageView;
    String imgsrc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image2);
        ///inti
        inti();
    }

    private void inti() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        imageView = (ImageView) findViewById(R.id.image);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();
        if (getIntent() != null) {
            imgsrc = getIntent().getStringExtra("img");
        }
        if (!TextUtils.isEmpty(imgsrc)) {
            String type = getIntent().getStringExtra("type");
            if (!TextUtils.isEmpty(type)) {
                if (!type.equals("bitmap"))
                    Glide.with(this).load(imgsrc).into(imageView);
                else
                    imageView.setImageURI(Uri.parse(imgsrc));
            } else {
                Glide.with(this).load(imgsrc).into(imageView);
            }
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}

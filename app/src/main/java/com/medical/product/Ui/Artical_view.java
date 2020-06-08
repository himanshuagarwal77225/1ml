package com.medical.product.Ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;

public class Artical_view extends AppCompatActivity {
    ImageView expandedImage;
    TextView title, date, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artical_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle("");

        intit();
        String id;
        String postTitle;
        String postContent;
        String image =null;

        if (getIntent() != null && getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
        if (getIntent() != null && getIntent().hasExtra("postTitle")) {
            postTitle = getIntent().getStringExtra("postTitle");
            title.setText(postTitle);
        }
        if (getIntent() != null && getIntent().hasExtra("postContent")) {
            postContent = getIntent().getStringExtra("postContent");
            desc.setText(stripHtml(postContent));
        }
        if (getIntent() != null && getIntent().hasExtra("image")) {
            image = getIntent().getStringExtra("image");
        }
        if (image == null && TextUtils.isEmpty(image)) {
            Glide.with(Artical_view.this)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(R.mipmap.ic_launcher)
                    .into(expandedImage);
        } else {
            Glide.with(Artical_view.this)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load("http://1ml.co.in/blog/wp-content/uploads/" + image)
                    .into(expandedImage);
        }


    }

    private void intit() {
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        desc = (TextView) findViewById(R.id.desc);
        expandedImage = (ImageView) findViewById(R.id.expandedImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_artical, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public static Spanned stripHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
            } else {
                return Html.fromHtml(html);
            }
        }
        return null;
    }

}

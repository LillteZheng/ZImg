package com.zhengsr.zimg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhengsr.zimglib.Zimg;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.img);
        Glide.with(this)
                .load("http://p1.pstatp.com/large/166200019850062839d3")
                .asBitmap()
                .placeholder(R.mipmap.load)
                .error(R.mipmap.fail)
                .into(imageView);


        Zimg.with(this.getApplicationContext());

    }
}

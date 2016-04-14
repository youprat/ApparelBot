package com.example.prat.apparelbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Carousel extends AppCompatActivity {

    PhotoManager photoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

    }

    @Override
    protected void onStart() {
        super.onStart();
        WebView imageHolder1 = (WebView) findViewById(R.id.imageHolder1);
        //imageHolder1.loadUrl();
    }
}

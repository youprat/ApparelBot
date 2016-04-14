package com.example.prat.apparelbot;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PhotoManager photoManager;
    EditText searchBox;
    String searchText;
    LinearLayout chat;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoManager = new PhotoManager(this);
        searchBox = (EditText) findViewById(R.id.searchEditText);

        TextView t1 = new TextView(MainActivity.this);
        t1.setText(R.string.greeting);
        t1.setBackgroundResource(R.drawable.rect_style_green);
        t1.setPadding(2,2,2,2);

        TextView t2 = new TextView(MainActivity.this);
        t2.setText(R.string.inquiry);
        t2.setBackgroundResource(R.drawable.rect_style_green);
        t2.setPadding(2,2,2,2);

        chat = (LinearLayout) findViewById(R.id.chatView);
        chat.addView(t1);
        chat.addView(t2);

        scrollView = (ScrollView) findViewById(R.id.scroller);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        ImageButton send = (ImageButton) findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = searchBox.getText().toString();
                searchBox.setText("");

                TextView t3 = new TextView(MainActivity.this);
                t3.setText(searchText);
                t3.setBackgroundResource(R.drawable.rect_style_blue);
                t3.setPadding(2,2,2,2);
                chat.addView(t3);

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

                photoManager.searchPhotosByText(searchText);
            }
        });
    }

    void updateChatViewWithText(){
        TextView t4 = new TextView(MainActivity.this);
        t4.setText(photoManager.tempResponse);
        t4.setBackgroundResource(R.drawable.rect_style_green);
        t4.setPadding(2,2,2,2);
        chat.addView(t4);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    void updateChatViewWithImage(Bitmap bmp){
//        ScrollingTabContainerView scrollTab = new ScrollingTabContainerView(this);
//        scrollTab.setLayoutParams(new LinearLayout.LayoutParams(350,350));

        ImageView  iv = new ImageView(MainActivity.this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(300,300));
        iv.setImageBitmap(bmp);
        iv.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v){
                // Start Carousel activity
            }
        });
//        scrollTab.addView(iv);

        chat.addView(iv);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
package com.example.prat.apparelbot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    User currentUser;
    PhotoManager photoManager;
    EditText searchBox;
    String searchText;
    LinearLayout chat;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            currentUser = new User("user");
            photoManager = new PhotoManager(this);
            searchBox = (EditText) findViewById(R.id.searchEditText);

            TextView t1 = new TextView(MainActivity.this);
            t1.setText(R.string.greeting);
            t1.setBackgroundResource(R.drawable.rect_style_green);
            t1.setPadding(4, 4, 4, 4);
            t1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView t2 = new TextView(MainActivity.this);
            t2.setText(R.string.inquiry);
            t2.setBackgroundResource(R.drawable.rect_style_green);
            t2.setPadding(4, 4, 4, 4);
            t2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

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
                    searchText = searchBox.getText().toString().trim();
                    searchBox.setText("");

                    TextView t3 = new TextView(MainActivity.this);
                    t3.setText(searchText);
                    t3.setBackgroundResource(R.drawable.rect_style_blue);
                    t3.setPadding(10, 10, 10, 10);
                    t3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    t3.setGravity(Gravity.END);
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
        } catch (Exception e) {e.getCause();}
    }

    void updateChatViewWithImage(Bitmap bmp){
        LinearLayout contain = new LinearLayout(this);
        contain.setOrientation(LinearLayout.VERTICAL);
        contain.setBackgroundResource(R.drawable.rect_style_green);
        contain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView tv = new TextView(this);
        tv.setText(R.string.click_to_view_all);
        tv.setPadding(4, 4, 4, 4);

        ImageView  iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        iv.setPadding(6, 10, 10, 10);
        iv.setImageBitmap(bmp);
        iv.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v){
                // Start Carousel activity
                Intent carouselIntent = new Intent(MainActivity.this, CarouselActivity.class);
                carouselIntent.putExtra("photoManager",photoManager);
                startActivity(carouselIntent);
            }
        });

        contain.addView(tv);
        contain.addView(iv);
        chat.addView(contain);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

}
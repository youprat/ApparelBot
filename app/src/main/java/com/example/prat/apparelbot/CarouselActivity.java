package com.example.prat.apparelbot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CarouselActivity extends AppCompatActivity {

    PhotoManager photoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        photoManager = getIntent().getExtras().getParcelable("photoManager");
        ArrayList<Photo> photoList = photoManager.photoList;

        for (Photo tmp: photoList) {
            photoManager.imageDownload(this, tmp.url);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void updateCarouselView(final Bitmap bmp){
        TextView tv = (TextView) findViewById(R.id.carouselSearchText);
        tv.setText("Search results...");
        LinearLayout carousel = (LinearLayout) findViewById(R.id.carouselView);

        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        l.setPadding(10,10,10,10);
        l.setVerticalGravity(Gravity.CENTER);
        l.setWeightSum(1.0f);

        ImageView iv = new ImageView(this);
        iv.setBackgroundResource(R.drawable.rect_style_white);
        iv.setPadding(10,10,10,10);
        iv.setImageBitmap(bmp);
        iv.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v){
//                // add to favorites
                Toast.makeText(CarouselActivity.this, "Favorited!", Toast.LENGTH_SHORT).show();
            }
        });
        l.addView(iv);

        carousel.addView(l);

        if(carousel.getChildCount() >= 10) {
            LinearLayout l1 = new LinearLayout(this);
            l1.setLayoutParams(new LinearLayout.LayoutParams(250,250));
            l1.setPadding(10,10,10,10);
            l1.setVerticalGravity(Gravity.CENTER);
            l1.setOrientation(LinearLayout.VERTICAL);
            l1.setWeightSum(1.0f);

            TextView tv1 = new TextView(this);
            tv1.setText("Show more");

            ImageView iv1 = new ImageView(this);
            iv1.setBackgroundResource(R.drawable.rect_style_white);
            iv1.setImageResource(R.drawable.showmore);
            iv1.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v){
                    //get 10 more images
                    photoManager.carouselAct = (CarouselActivity) v.getContext();
                    photoManager.nDownloadsStarted.set(0);
                    photoManager.getNextPage();

                    Intent carouselIntent = new Intent(photoManager.carouselAct, CarouselActivity.class);
                    carouselIntent.putExtra("photoManager", photoManager);
                    while(photoManager.nDownloadsDone.get() < 10) {}
                    startActivity(carouselIntent);
                }
            });
            l1.addView(tv1);
            l1.addView(iv1);

            carousel.addView(l1);
        }
    }

    void showNextCarousel(){

    }
}

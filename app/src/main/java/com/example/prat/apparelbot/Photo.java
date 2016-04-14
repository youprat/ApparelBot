package com.example.prat.apparelbot;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prat on 4/14/2016.
 */
public class Photo implements Parcelable {
        String ID;
        String url;
        Bitmap image;
        JSONObject json;

        Photo(JSONObject j) {
            try {
                json = j;
                ID = (String) json.get("id");
                url = (String) json.get("url_m");
            } catch (JSONException e){e.getCause();}
        }

        public void setImage(Bitmap bmp){
            image = bmp;
        }

        public Bitmap getImage(){
            return image;
        }

        Photo(Parcel parcel){
            readFromParcel(parcel);
        }

        public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
            public Photo createFromParcel(Parcel in ) {
                return new Photo(in);
            }

            public Photo[] newArray(int size) {
                return new Photo[size];
            }
        };


        @Override
        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int i){
            dest.writeString(ID);
            dest.writeString(url);
        }

        public void readFromParcel(Parcel in){
            ID = in.readString();
            url = in.readString();
        }
}


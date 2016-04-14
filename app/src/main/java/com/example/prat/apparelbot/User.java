package com.example.prat.apparelbot;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Prat on 4/14/2016.
 */
public class User implements Parcelable{
    String userName;
    ArrayList<Photo> favorites;

    User(){
        favorites = new ArrayList<>();
    }

    User(String uName){
        this();
        userName = uName;
    }

    User(Parcel parcel){
        readFromParcel(parcel);
    }

    public void addToFavorites(Photo favPhoto){
        if(favPhoto!=null){
            favorites.add(favPhoto);
        }
    }

    public ArrayList<Photo> getFavorites(){
        return favorites;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in ) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i){
        dest.writeString(userName);
        dest.writeTypedList(favorites);
    }

    public void readFromParcel(Parcel in){
        userName = in.readString();
        in.readTypedList(favorites, Photo.CREATOR);
    }
}

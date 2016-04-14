package com.example.prat.apparelbot;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Prat on 4/13/2016.
 */
public class PhotoManager implements NetworkManager.AsyncResponse, Parcelable {
    MainActivity mainAct;
    CarouselActivity carouselAct = null;
    ArrayList<Photo> photoList;
    int page = 1;
    String searchString;
    String tagString;
    User user;
    AtomicBoolean searchStarted = new AtomicBoolean(false);
    AtomicInteger nDownloadsStarted = new AtomicInteger(0);
    AtomicBoolean searchDone = new AtomicBoolean(false);
    AtomicInteger nDownloadsDone = new AtomicInteger(0);

    PhotoManager(){
        photoList = new ArrayList<>();
    }

    PhotoManager(MainActivity ma){
        this();
        mainAct = ma;
    }

    PhotoManager(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public void setUser(User u){
        user = u;
    }

    public void searchPhotosByText(String searchText){
        searchString = searchText;
        photoList.clear();
        NetworkManager netHandler = new NetworkManager(this);
        String url = netHandler.buildQueryString(searchText, false, page);
        searchStarted.set(true);
        netHandler.execute(url);
    }

    public void searchPhotosByTag(String tagText){
        tagString = tagText;
        NetworkManager netHandler = new NetworkManager(this);
        String url = netHandler.buildQueryString(tagText, false, page);
        searchStarted.set(true);
        netHandler.execute(url);
    }

    public void getNextPage(){
        page++;
        searchPhotosByText(searchString);
    }

    @Override
    public void onFinish(JSONObject response){
        parseResponse(response);

        if(photoList!=null) {
            Photo p = photoList.get(0);
            imageDownload(p.url);
        }
        searchDone.set(true);
    }

    @Override
    public void onFinishDownload(Bitmap b) {
        if(carouselAct!=null) {
            carouselAct.updateCarouselView(b);
        } else if (mainAct!=null){
            mainAct.updateChatViewWithImage(b);
        }
        nDownloadsDone.incrementAndGet();
    }

    public void parseResponse(JSONObject response) {
        try {
                JSONObject parse = (JSONObject) response.get("photos");
                JSONArray jPhotoArray = (JSONArray) parse.get("photo");
                for (int i = 0; i < jPhotoArray.length(); i++) {
                    JSONObject jPhoto = jPhotoArray.getJSONObject(i);
                    Photo p = new Photo(jPhoto);
                    photoList.add(p);
                }

            }catch(Exception e){e.getCause();}
    }

    public void imageDownload(String url){
        carouselAct = null;
        (new NetworkManager()).new ImageDownloader(this).execute(url);
    }

    public void imageDownload(CarouselActivity carousel, String url){
        carouselAct = carousel;
        (new NetworkManager()).new ImageDownloader(this).execute(url);
    }

    public static final Parcelable.Creator<PhotoManager> CREATOR = new Parcelable.Creator<PhotoManager>() {
        public PhotoManager createFromParcel(Parcel in ) {
            return new PhotoManager( in );
        }

        public PhotoManager[] newArray(int size) {
            return new PhotoManager[size];
        }
    };


    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i){
        dest.writeString(searchString);
        dest.writeInt(page);
        dest.writeTypedList(photoList);
    }

    public void readFromParcel(Parcel in){
        searchString = in.readString();
        page = in.readInt();
        in.readTypedList(photoList, Photo.CREATOR);
    }
}

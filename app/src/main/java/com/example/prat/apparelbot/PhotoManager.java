package com.example.prat.apparelbot;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prat on 4/13/2016.
 */
public class PhotoManager implements NetworkManager.AsyncResponse {
    MainActivity mainAct;
    ArrayList<Photo> photoList;
    String searchText;
    String tempResponse = "<empty>";

    class Photo {
        String ID;
        String url;
//        Bitmap image;
        JSONObject json;

        Photo(JSONObject j) {
            try {
                json = j;
                ID = (String) json.get("id");
                url = (String) json.get("url_m");
            } catch (JSONException e){e.getCause();}
        }
    }

    PhotoManager(MainActivity ma){
        mainAct = ma;
        photoList = new ArrayList<>();
    }

    public void searchPhotosByText(String searchText){
        photoList.clear();
        NetworkManager netHandler = new NetworkManager(this);
        String url = netHandler.buildQueryString(searchText, false, 1);
        netHandler.execute(url);
    }

    public void searchPhotosByTag(String tagText){
        NetworkManager netHandler = new NetworkManager(this);
        String url = netHandler.buildQueryString(tagText, false, 1);
        netHandler.execute(url);
    }

    public void getNextPage(){

    }

    @Override
    public void onFinish(JSONObject response){
        parseResponse(response);
        if(response!=null)
            tempResponse = response.toString();

        for (Photo tmp: photoList) {
            imageDownload(tmp.url);
        }
        //mainAct.updateChatViewWithText();
    }

    @Override
    public void onFinishDownload(Bitmap b) {
        mainAct.updateChatViewWithImage(b);
    }

    public void parseResponse(JSONObject response) {
        try {
            if (response != null) {
                tempResponse = response.toString();
                JSONObject parse = (JSONObject) response.get("photos");
                JSONArray jPhotoArray = (JSONArray) parse.get("photo");
                for (int i = 0; i < jPhotoArray.length(); i++) {
                    JSONObject jPhoto = jPhotoArray.getJSONObject(i);
                    Photo p = new Photo(jPhoto);
                    photoList.add(p);
                }
            }
            }catch(Exception e){e.getCause();}
        }

    public void imageDownload(String url){
        (new NetworkManager()).new ImageDownloader(this).execute(url);
    }
}

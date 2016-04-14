package com.example.prat.apparelbot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Prat on 4/13/2016.
 */
class NetworkManager extends AsyncTask<String, Integer, JSONObject> {
    private static final String DEBUG_TAG = "Debug";
    private String flickrApiKey= "a91ebdd4d2f59465429f5e7667b1ef9c";
    private String secret = "c6ba2d764efb4584";

    String queryUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&safe_search=1&extras=url_m";
    String perPage = "&per_page=10";
    String page = "&page=";
    String format = "&format=json&nojsoncallback=?";
    String tag = "&tags=";
    String search = "&text=";
    String apiKey = "&api_key=";

    JSONObject responseObj;

    public interface AsyncResponse {
        void onFinish(JSONObject response);
        void onFinishDownload(Bitmap image);
    }

    public AsyncResponse delegate;

    public NetworkManager() {}

    public NetworkManager(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    String buildQueryString(String searchText, boolean isTag, int pageNumber){
        String query;
        if(isTag){
            query = queryUrl + page + pageNumber + perPage + format + apiKey + flickrApiKey + tag + searchText + apiKey + flickrApiKey;
        }
        else {
            query = queryUrl + search + searchText + perPage + page + pageNumber + format + apiKey + flickrApiKey;
        }
        return query;
    }

    JSONObject connection(String requestUrl) {
        InputStream is;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            String response = conn.getResponseMessage();
            Log.d(DEBUG_TAG, "The response is: " + responseCode + ": " + response);
            is = conn.getInputStream();

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String resLine = br.readLine();
                br.close();
                responseObj = new JSONObject(resLine);
            } catch (JSONException e) {

            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (is != null) {
                is.close();
            }

            return responseObj;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected JSONObject doInBackground(String... qs) {
        JSONObject response = connection(qs[0]);
        responseObj = response;
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response){
        delegate.onFinish(response);
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        PhotoManager delegate;

        ImageDownloader(PhotoManager pm) {
            delegate = pm;
        }

        @Override
        public Bitmap doInBackground(String... urlString){
            Bitmap bmp = null;
            try{
                    URL url = new URL(urlString[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    bmp = BitmapFactory.decodeStream(bis);
                    } catch (Exception e) {e.getCause();}
            return bmp;
        }

        @Override
        public void onPostExecute(Bitmap bmp){
            delegate.onFinishDownload(bmp);
        }
    }

}
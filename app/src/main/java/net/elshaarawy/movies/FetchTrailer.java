package net.elshaarawy.movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by elshaarawy on 26-Apr-16.
 */
public class FetchTrailer extends AsyncTask<String, Integer, Uri> {



    private Uri parseData(String JSONString) throws JSONException {
        JSONObject serverResponse = new JSONObject(JSONString);
        JSONArray resultArray = serverResponse.getJSONArray("results");
        JSONObject firstTrailer = resultArray.getJSONObject(0);
        String trailerKey = firstTrailer.getString("key");
        return Uri.parse("https://www.youtube.com/watch").buildUpon().appendQueryParameter("v",trailerKey).build();
    }

    @Override
    protected Uri doInBackground(String... params) {
        String unparsedData="";
        BufferedReader mBufferReader = null;
        HttpURLConnection mURLConnection = null;




        try {
            String my_key = "09531d99ec8e1c8dc6594e0b5928b2c6";

            final String baseURL = "https://api.themoviedb.org/3/movie/";

            String link = Uri.parse(baseURL).buildUpon().appendPath(params[0]).appendPath("videos").
                    appendQueryParameter("api_key", my_key).build().toString();

            URL url = new URL(link);

            Log.v("URL", url.toString());


            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setRequestMethod("GET");
            mURLConnection.connect();

            InputStream mInputStream = mURLConnection.getInputStream();

            mBufferReader = new BufferedReader(new InputStreamReader(mInputStream));

            StringBuffer mStringBuffer = new StringBuffer();

            String line;
            while ((line = mBufferReader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                mStringBuffer.append(line + "\n");
            }

            unparsedData = mStringBuffer.toString();
            Log.v("moh", unparsedData);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (mURLConnection != null) {
                mURLConnection.disconnect();
            }
            if (mBufferReader != null) {

                try {
                    mBufferReader.close();
                } catch (final IOException e) {
                    Log.e("elshaarawy", "Error closing stream", e);
                }

            }
        }

        try {
            return parseData(unparsedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}

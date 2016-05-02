package net.elshaarawy.movies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import net.elshaarawy.movies.data.MoviesContract.MoviesEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by elshaarawy on 23-Apr-16.
 */
public class FetchMovies extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private String ORDER_TYPE;
    private Utility utility = new Utility();
    private BufferedReader mBufferReader = null;
    private HttpURLConnection mURLConnection = null;
    private final String api_key = "api_key";
    private final String my_key = "09531d99ec8e1c8dc6594e0b5928b2c6";
    private final String baseURL = "https://api.themoviedb.org/3/movie/";

    public FetchMovies(Context context, String order) {
        this.mContext = context;
        this.ORDER_TYPE = order;
    }

    private void parseData(String JSONString) throws JSONException {
        Uri INSERTION_RUI = utility.isPopular(ORDER_TYPE) ? MoviesEntry.CONTENT_URI_POPULAR : MoviesEntry.CONTENT_URI_RATED;

        try {

            JSONObject serverResponse = new JSONObject(JSONString);

            JSONArray resultArray = serverResponse.getJSONArray("results");

            int resultArrayLength = resultArray.length();

            Log.v("hint", serverResponse.toString());
            Log.v("hint", resultArray.toString());

            int page = serverResponse.getInt("page");

            String mPosterURL, mOverview, mReleaseDate, mID, mTitle, mVoteCount, mVoteAverage,mJsonReviews;

            Vector<ContentValues> cvVector = new Vector<ContentValues>(resultArrayLength);

            for (int i = 0; i < resultArrayLength; i++) {

                JSONObject movie = resultArray.getJSONObject(i);

                mPosterURL = "https://image.tmdb.org/t/p/w396/" + movie.getString("poster_path");

                mOverview = movie.getString("overview");
                mReleaseDate = movie.getString("release_date");
                mID = movie.getString("id");
                mTitle = movie.getString("title");
                mVoteCount = movie.getString("vote_count");
                mVoteAverage = movie.getString("vote_average");
                mJsonReviews = fetchReviews(mID);

                ContentValues movieValues = new ContentValues();
                movieValues.put(MoviesEntry.COLUMN_PAGE, page);
                movieValues.put(MoviesEntry.COLUMN_IMG_URL, mPosterURL);
                movieValues.put(MoviesEntry.COLUMN_OVERVIEW, mOverview);
                movieValues.put(MoviesEntry.COLUMN_RELEASE_DATE, mReleaseDate);
                movieValues.put(MoviesEntry.COLUMN_ID, mID);
                movieValues.put(MoviesEntry.COLUMN_TITLE, mTitle);
                movieValues.put(MoviesEntry.COLUMN_VOTE_COUNT, mVoteCount);
                movieValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, mVoteAverage);
                movieValues.put(MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY, mJsonReviews);

                cvVector.add(movieValues);

            }
            if (cvVector.size() > 0) {
                ContentValues[] contentValues = new ContentValues[cvVector.size()];
                cvVector.toArray(contentValues);
                mContext.getContentResolver().delete(INSERTION_RUI,"",null);
                mContext.getContentResolver().bulkInsert(INSERTION_RUI, contentValues);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(String... params) {
        String moviesString = null;
        final String page = "page";
        try {
            final String page_number = params[0].equals("0") ? "1" : params[0];

            String link = Uri.parse(baseURL).buildUpon().appendPath(ORDER_TYPE).appendQueryParameter(page, page_number)
                    .appendQueryParameter(api_key, my_key).build().toString();

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
                mStringBuffer.append(line + "\n");
            }

            moviesString = mStringBuffer.toString();

            Log.v("moh", moviesString);
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
            parseData(moviesString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  String fetchReviews (String movieID){
        String serverResponse = null;
        String reviewsArray=null;
        try {
            String link = Uri.parse(baseURL).buildUpon().appendPath(movieID).appendPath("reviews")
                    .appendQueryParameter(api_key, my_key).build().toString();

            URL url = new URL(link);

            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setRequestMethod("GET");
            mURLConnection.connect();

            InputStream mInputStream = mURLConnection.getInputStream();

            mBufferReader = new BufferedReader(new InputStreamReader(mInputStream));

            StringBuffer mStringBuffer = new StringBuffer();

            String line;
            while ((line = mBufferReader.readLine()) != null) {
                mStringBuffer.append(line + "\n");
            }
            serverResponse = mStringBuffer.toString();
            JSONObject serverResponseObject = new JSONObject(serverResponse);
            reviewsArray = serverResponseObject.getJSONArray("results").toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
        return reviewsArray;
    }
}

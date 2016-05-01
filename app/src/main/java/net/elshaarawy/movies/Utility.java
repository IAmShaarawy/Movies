package net.elshaarawy.movies;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.FrameLayout;

import net.elshaarawy.movies.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elshaarawy on 23-Apr-16.
 */
public class Utility {

    public static final String APP_HASH_TAG="#android #elshaarawy #MCIT #udacity #google #TIEC #wuzzuf #MAL4EG";
    public static final String [] MOVIE_DETAILS_COLUMNS={

            MoviesContract.MoviesEntry.COLUMN_IMG_URL,
            MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_ID,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY,

    };

    public static boolean isPopular(String orderType){
        boolean isPopular;
        switch (orderType){
            case "popular":
                isPopular = true;
                break;
            default:
            case "top_rated":
                isPopular=false;
                break;
        }
        return isPopular;
    }

    public static String formattedReviews(String unFormattedReviews){
        String separator="____________________________________________________";
        String formattedReviews="______________________REVIEWS_______________________"+"\n";
        try {

            String author,content;
            JSONObject review;
            JSONArray resultArray = new JSONArray(unFormattedReviews);
            if (resultArray.length()>0){
                for (int i =1;i<=resultArray.length();i++){
                    review = resultArray.getJSONObject(i);
                    author = review.getString("author");
                    content =review.getString("content");

                    formattedReviews += "("+i+")"+"►"+author+" said :\n\n"+content+"\n"+separator+"\n";
                }
            }
            else {
                formattedReviews += "No Reviews Available\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formattedReviews+"________________________END_________________________";
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mConnectivityManager.getActiveNetworkInfo() != null;
    }

    public static boolean isLandscape(Activity activity){
        FrameLayout detailsFragmentLand = (FrameLayout) activity.findViewById(R.id.detailsFragmentLand);
        return detailsFragmentLand == null ? false : true;
    }
    public static boolean isTap(Activity activity){
        FrameLayout detailsFragmentTap = (FrameLayout) activity.findViewById(R.id.detailsFragmentTap);
        return detailsFragmentTap==null?false:true;
    }

}

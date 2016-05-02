package net.elshaarawy.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by elshaarawy on 19-Apr-16.
 */
public class MoviesContract {

    // Content provider
    public static final String CONTENT_AUTHORITY = "net.elshaarawy.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String POPULAR_BATH = "popular";

    public static final String RATED_BATH = "rated";

    public static final String FAVORITE_BATH = "favorite";

    public static final class MoviesEntry implements BaseColumns {

        // Database
        public static final String TABLE_NAME_POPULAR = "popular_movies";

        public static final String TABLE_NAME_RATED = "rated_movies";

        public static final String TABLE_NAME_FAVORITE = "favorite_movies";

        public static final String COLUMN_PAGE = "page";

        public static final String COLUMN_IMG_URL = "imgURL";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_REVIEWS_JSON_ARRAY = "reviews";




        //Content Provider URIs
        public static final Uri CONTENT_URI_POPULAR = BASE_CONTENT_URI.buildUpon().appendPath(POPULAR_BATH).build();

        public static final Uri CONTENT_URI_RATED = BASE_CONTENT_URI.buildUpon().appendPath(RATED_BATH).build();

        public static final Uri CONTENT_URI_FAVORITE = BASE_CONTENT_URI.buildUpon().appendPath(FAVORITE_BATH).build();

        //mime type values
        public static final String POPULAR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+POPULAR_BATH;// DIR for multi items
        public static final String RATED_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+RATED_BATH;
        public static final String FAVORITE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+FAVORITE_BATH;
        public static final String POPULAR_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+POPULAR_BATH;
        public static final String RATED_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+RATED_BATH;
        public static final String FAVORITE_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+FAVORITE_BATH;


        //attach _id to URIs

        public static Uri append_idToURI(Uri uri ,Long _id ){
            return ContentUris.withAppendedId(uri,_id);
        }
        public static Uri itemUri(String item_id,boolean isPopular){
            Uri uri =null;
            if (item_id!=null){
                 uri = isPopular?Uri.withAppendedPath(CONTENT_URI_POPULAR,item_id):Uri.withAppendedPath(CONTENT_URI_RATED,item_id);
            }
             return uri;
        }






    }

}

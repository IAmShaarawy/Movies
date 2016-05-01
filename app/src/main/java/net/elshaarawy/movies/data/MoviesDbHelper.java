package net.elshaarawy.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.elshaarawy.movies.data.MoviesContract.MoviesEntry;

/**
 * Created by elshaarawy on 19-Apr-16.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION=8;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_POPULAR_MOVIES_TABLE = " CREATE TABLE " + MoviesEntry.TABLE_NAME_POPULAR + " ( "+
                MoviesEntry._ID + " INTEGER PRIMARY KEY  AUTOINCREMENT , "+
                MoviesEntry.COLUMN_PAGE + " INTEGER NOT NULL , "+
                MoviesEntry.COLUMN_IMG_URL + " TEXT NOT NULL , " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_RELEASE_DATE+ " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_ID+" TEXT NOT NULL  , "+
                MoviesEntry.COLUMN_TITLE+" TEXT NOT NULL , "+
                MoviesEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_VOTE_AVERAGE+" TEXT NOT NULL , "+
                MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY+" TEXT NOT NULL ); ";

        final String CREATE_RATED_MOVIES_TABLE = " CREATE TABLE " + MoviesEntry.TABLE_NAME_RATED + " ( "+
                MoviesEntry._ID + " INTEGER PRIMARY KEY  AUTOINCREMENT , "+
                MoviesEntry.COLUMN_PAGE + " INTEGER NOT NULL , "+
                MoviesEntry.COLUMN_IMG_URL + " TEXT NOT NULL , " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_RELEASE_DATE+ " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_ID+" TEXT NOT NULL  , "+
                MoviesEntry.COLUMN_TITLE+" TEXT NOT NULL , "+
                MoviesEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_VOTE_AVERAGE+" TEXT NOT NULL , "+
                MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY+" TEXT NOT NULL ); ";

        final String CREATE_FAVORITE_MOVIES_TABLE = " CREATE TABLE " + MoviesEntry.TABLE_NAME_FAVORITE + " ( "+
                MoviesEntry._ID + " INTEGER PRIMARY KEY  AUTOINCREMENT , "+
                MoviesEntry.COLUMN_IMG_URL + " TEXT NOT NULL , " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_RELEASE_DATE+ " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_ID+" TEXT NOT NULL  , "+
                MoviesEntry.COLUMN_TITLE+" TEXT NOT NULL , "+
                MoviesEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL , "+
                MoviesEntry.COLUMN_VOTE_AVERAGE+" TEXT NOT NULL , "+
                MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY+" TEXT NOT NULL ); ";

        db.execSQL(CREATE_POPULAR_MOVIES_TABLE);
        db.execSQL(CREATE_RATED_MOVIES_TABLE);
        db.execSQL(CREATE_FAVORITE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+MoviesEntry.TABLE_NAME_POPULAR);
        db.execSQL("DROP TABLE IF EXISTS "+MoviesEntry.TABLE_NAME_RATED);
        db.execSQL("DROP TABLE IF EXISTS "+MoviesEntry.TABLE_NAME_FAVORITE);
        onCreate(db);

    }
}

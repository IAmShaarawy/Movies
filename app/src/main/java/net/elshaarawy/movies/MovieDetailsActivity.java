package net.elshaarawy.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
    }

    public static Intent sendIntent(Context context, Uri uri, String id) {
        Intent mIntent = new Intent(context, MovieDetailsActivity.class);
        mIntent.setData(uri);
        mIntent.putExtra("id", id);
        return mIntent;
    }
}

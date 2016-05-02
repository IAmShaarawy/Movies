package net.elshaarawy.movies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MoviesMainActivity extends AppCompatActivity implements MovieListener {
    boolean isLandscape, isTap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_movies_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isLandscape = Utility.isLandscape(this);
        isTap = Utility.isTap(this);
        MoviesMainFragment moviesMainFragment = new MoviesMainFragment();
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        moviesMainFragment.setMovieListener(this);

        if (isLandscape) {
            getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentLand, moviesMainFragment).commit();

        } else if (isTap) {
            getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentTap, moviesMainFragment).commit();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentPort, moviesMainFragment).commit();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void setSelectedMovie(Uri movieDetailsURI, String ID, boolean startActivity) {

        if (isLandscape) {
            MovieDetailsFragment movieDetailsFragmentLand = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("detailsURI", movieDetailsURI.toString());
            bundle.putString("movieID", ID);
            movieDetailsFragmentLand.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsFragmentLand, movieDetailsFragmentLand).commit();
            TextView textView = (TextView) findViewById(R.id.landscapePlaceholder);
            textView.setVisibility(View.GONE);

        } else if (isTap) {
            MovieDetailsFragment movieDetailsFragmentTap = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("detailsURI", movieDetailsURI.toString());
            bundle.putString("movieID", ID);
            movieDetailsFragmentTap.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsFragmentTap, movieDetailsFragmentTap).commit();
            TextView textView = (TextView) findViewById(R.id.landscapePlaceholder);
            textView.setVisibility(View.GONE);

        } else {
            Intent intent = MovieDetailsActivity.sendIntent(this, movieDetailsURI, ID);
            if (startActivity)
                startActivity(intent);
        }

    }
}

package net.elshaarawy.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.elshaarawy.movies.data.MoviesContract;

import java.util.concurrent.ExecutionException;


/**
 * Created by elshaarawy on 25-Apr-16.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri mURI;
    private TextView headingTV, date_TV, voteCount_TV, rating_TV, reviews_TV, overview_TV;
    private Button favorite_BTN, trailer_BTN;
    private ImageView poster_imgV;
    private FloatingActionButton fab;
    private String heading, date, voteCount, rating, unFormattedReviews, overview, posterURL, movieID, year, SHARE_TEXT;


    private boolean FAVORITE_FLAG;
    private Uri FAVORITE_URI;

    private FetchTrailer FT;

    private static final int MOVIE_DETAILS_LOADER_ID = 0;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_details, menu);


        MenuItem menuItem = menu.findItem(R.id.action_settings);
        if (Utility.isLandscape(getActivity())||Utility.isTap(getActivity()))
            menuItem.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        if (savedInstanceState==null)
        setHasOptionsMenu(true);
        String ID;
        if (getActivity().findViewById(R.id.detailsFragmentLand) != null || getActivity().findViewById(R.id.detailsFragmentTap) != null) {


            mURI = Uri.parse(getArguments().getString("detailsURI"));
            ID = getArguments().getString("movieID");

        }
        else {
            Intent mIntent = getActivity().getIntent();
            mURI = mIntent.getData();
            ID = mIntent.getStringExtra("id");
        }
        FAVORITE_URI = MoviesContract.MoviesEntry.CONTENT_URI_FAVORITE.buildUpon().appendEncodedPath(ID).build();
        if (ID != null) {
            Cursor cursor = getActivity().getContentResolver().query(FAVORITE_URI, Utility.MOVIE_DETAILS_COLUMNS, null, null, null);

//        Toast.makeText(getContext(),"Staus "+cursor.getCount()+" "+ ID,Toast.LENGTH_SHORT).show();
            FAVORITE_FLAG = cursor.getCount() != 0 ? true : false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.movie_details, container, false);

        //text views
        headingTV = (TextView) view.findViewById(R.id.heading);
        date_TV = (TextView) view.findViewById(R.id.date);
        voteCount_TV = (TextView) view.findViewById(R.id.vote_count);
        rating_TV = (TextView) view.findViewById(R.id.rating);
        overview_TV = (TextView) view.findViewById(R.id.overview);
        reviews_TV = (TextView) view.findViewById(R.id.reviews);
        //Buttons
        favorite_BTN = (Button) view.findViewById(R.id.favorite);
        trailer_BTN = (Button) view.findViewById(R.id.trailer);
        //image view
        poster_imgV = (ImageView) view.findViewById(R.id.poster);

        if (FAVORITE_FLAG) {
            favorite_BTN.setBackgroundColor(getResources().getColor(R.color.khaki));
            favorite_BTN.setText("Loved");
        }
        favorite_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                if (!FAVORITE_FLAG) {

                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_IMG_URL, posterURL);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, date);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_ID, movieID);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, heading);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, voteCount);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, rating);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY, unFormattedReviews);

                    getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI_FAVORITE, contentValues);

                    favorite_BTN.setBackgroundColor(getResources().getColor(R.color.khaki));
                    favorite_BTN.setText("Loved");
                    Toast.makeText(getContext(), "You Love " + heading, Toast.LENGTH_SHORT).show();
                    FAVORITE_FLAG = !FAVORITE_FLAG;
                } else {
                    int i = getActivity().getContentResolver().delete(FAVORITE_URI, null, null);
                    favorite_BTN.setBackgroundColor(getResources().getColor(R.color.favorite_bg));
                    favorite_BTN.setText("mark as\nfavorite");
                    FAVORITE_FLAG = !FAVORITE_FLAG;
//                    Toast.makeText(getContext(),"deleted = "+i,Toast.LENGTH_LONG).show();
                }
            }
        });


        trailer_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(getActivity())) {
                    FT = new FetchTrailer();
                    FT.execute(movieID);
                    Uri trailerURI = null;
                    try {
                        trailerURI = FT.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    playTrailer(trailerURI);
                } else {
                    Snackbar.make(view, "check your Internet Connection", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        reviews_TV.setOnClickListener(new View.OnClickListener() {
            boolean REVIEWS_FLAG = true;

            @Override
            public void onClick(View v) {

                if (REVIEWS_FLAG) {
                    String formattedReviews = Utility.formattedReviews(unFormattedReviews);
                    reviews_TV.setText(formattedReviews);
                    reviews_TV.setTextColor(getResources().getColor(R.color.reviewsText));
                    REVIEWS_FLAG = !REVIEWS_FLAG;
                } else {
                    reviews_TV.setText(getString(R.string.reviewDefaultText));
                    reviews_TV.setTextColor(getResources().getColor(R.color.colorPrimary));
                    REVIEWS_FLAG = !REVIEWS_FLAG;
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabShare);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(shareMovie());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursor;
//        if (mIntent == null) {
//            return null;
//        }
        if (mURI != null) {
            cursor = new CursorLoader(getContext(), mURI, Utility.MOVIE_DETAILS_COLUMNS, null, null, null);
        } else cursor = null;
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        heading = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE));
        date = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE));
        voteCount = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT));
        rating = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE));
        unFormattedReviews = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEWS_JSON_ARRAY));
        overview = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW));
        posterURL = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMG_URL));
        movieID = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ID));

        year = date.substring(0, 4);

        headingTV.setText(heading);
        date_TV.setText(date.substring(0, 4));
        voteCount_TV.setText(voteCount + " votes");
        rating_TV.setText(rating + "/10");
        overview_TV.setText(overview);
        Picasso.with(getContext()).load(posterURL).
                placeholder(R.drawable.loading).
                error(R.drawable.noposter).
                into(poster_imgV);
        if (heading.length() > 14)
            getActivity().setTitle(heading.substring(0, 14) + "...");
        else
            getActivity().setTitle(heading);


        SHARE_TEXT = "Movie: " + heading +
                "\nRelease Year: " + year +
                "\nRating: " + rating +
                "\n Poster: " + posterURL +
                "\n" + Utility.APP_HASH_TAG;

//        Toast.makeText(getContext(),"onLoadFinished",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void playTrailer(Uri uri) {
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(uri);
        if (trailerIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(trailerIntent);
        }
    }

    private Intent shareMovie() {


        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, SHARE_TEXT);

        return shareIntent;
    }
}

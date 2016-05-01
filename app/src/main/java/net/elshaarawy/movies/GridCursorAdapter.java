package net.elshaarawy.movies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.elshaarawy.movies.data.MoviesContract;

/**
 * Created by elshaarawy on 23-Apr-16.
 */
public class GridCursorAdapter extends CursorAdapter {


    public GridCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    static class Holder {
        public final ImageView poster_img;

        Holder(View view) {
            this.poster_img = ( ImageView ) view.findViewById(R.id.poster_image_view);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Holder holder = (Holder) view.getTag();


        final String IMG_URL = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMG_URL));

        Picasso.with(context).load(IMG_URL).
                placeholder(R.drawable.loading).
                error(R.drawable.noposter).
                into(holder.poster_img);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.poster,parent,false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }
}

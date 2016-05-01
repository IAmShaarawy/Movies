package net.elshaarawy.movies;

import android.net.Uri;

/**
 * Created by elshaarawy on 30-Apr-16.
 */
public interface MovieListener {
    public void setSelectedMovie(Uri movieDetailsURI,String ID,boolean startActivity);
}

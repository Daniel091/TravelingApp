package prak.travelerapp.FlickrAPI;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import prak.travelerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LandingActivityFragment extends Fragment {

    public LandingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landing, container, false);
    }
}

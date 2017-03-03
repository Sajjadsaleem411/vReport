package app.vreport.com.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.vreport.com.R;

/**
 * Created by Sajjad Saleem on 1/4/2017.
 */
public class NullFragment extends Fragment {
        public NullFragment(){

        }
    public static NullFragment newInstance() {
        NullFragment fragment = new NullFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_main_list, container, false);

        return inflatedView;

    }
}
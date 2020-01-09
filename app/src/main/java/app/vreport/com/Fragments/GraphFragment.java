package app.vreport.com.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;

import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.Controller.location;
import app.vreport.com.Model.Resources;
import app.vreport.com.R;

/**
 * Created by Sajjad Saleem on 1/4/2017.
 */
public class GraphFragment extends Fragment {

    EditText search_area;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    location mlocation;
    String[] address;
    Resources resources ;
    BarChart mChart;
    View mView;

    float[] dummy={20.0f,10.0f,40.0f,8.0f,5.0f};
    public float[] data;

    public GraphFragment() {

        address=new String[3];
        resources=new Resources();
        data=new float[resources.category_graph.length];
        data=SplashScreen.sql.getGraphData(null);


    }

    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_graph, container, false);

        mView=inflatedView;

        mChart = (BarChart) mView.findViewById(R.id.barChart);
        mlocation=new location(getContext());
        Search_area();
        Bar_graph();

        return inflatedView;

    }

    public void Bar_graph(){

        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i=0;i<resources.category_graph.length;i++){

            entries.add(new BarEntry(data[i], i));
        }

        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setBarSpacePercent(50f);
        dataset.setColor(R.color.colorPrimary);


        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0;i<resources.category_graph.length;i++){

            labels.add(resources.category_graph[i]);

        }
        BarData data = new BarData(labels, dataset);
        data.setGroupSpace(10f);
        mChart.setData(data); // set the data and list of lables into chart

        mChart.setDescription("");  // set the description
        mChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = mChart.getXAxis();

        xAxis.setDrawGridLines(true);
//        xAxis.setAxisLineWidth(50f);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);


    }
    public void Search_area(){
        search_area = (EditText) mView.findViewById(R.id.search_graph);
        search_area.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //      performSearch();
                    return true;
                }
                return false;
            }
        });
        search_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();

                    intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(getActivity());
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == getActivity().RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getContext(), data);
                    Log.i("Tag", "Place: " + place.getName());
                    Log.d("Tag", "address" + place.getAddress());
                    SetData((String) place.getName());

                    String[] add=mlocation.getLocationFromAddress((String) place.getAddress());

                    search_area.setText(place.getName());


                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getContext(), data);
                    // TODO: Handle the error.
                    Log.i("Tag", status.getStatusMessage());

                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "ListFragment_onActivity "+e,
                    Toast.LENGTH_SHORT).show();

        }
    }
    public  void SetData(String place){

        data=SplashScreen.sql.getGraphData(place);
        Bar_graph();
    }
}
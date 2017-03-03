package app.vreport.com.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ramotion.foldingcell.FoldingCell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.Controller.FoldingCellListAdapter;
import app.vreport.com.Model.Item;
import app.vreport.com.Model.Report;
import app.vreport.com.Model.Resources;
import app.vreport.com.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MainListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListFragment extends Fragment {

    EditText editText;
    String category="",place="",city="";

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Button spinner;
    Resources resources ;
    FoldingCellListAdapter adapter;
    ListView theListView;

    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        return fragment;
    }

    public MainListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   /*     FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
   */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_main_list, container, false);

        try {

            resources = new Resources();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, resources.category_dropdown);

            spinner=(Button)inflatedView.findViewById(R.id.category);
            spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Select by area")
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // TODO: user specific action
                                    try {


                                        if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[0])) {
                                            category=resources.name[0][0];
                                            CallAdapter();

                                        } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[1])) {
                                            category=resources.name[1][0];
                                            CallAdapter();

                                        } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[2])) {
                                            category=resources.name[2][0];
                                            CallAdapter();

                                        } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[3])) {
                                            category=resources.name[3][0];
                                            CallAdapter();

                                        } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[4])) {
                                            category=resources.name[4][0];
                                            CallAdapter();
                                        } else {
                                            category="";
                                            CallAdapter();


                                        }}catch (Exception e){
                                        Toast.makeText(getContext(), "ListFragment_onselectmethode "+e,
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });
            theListView = (ListView) inflatedView.findViewById(R.id.mainListView);

            editText = (EditText) inflatedView.findViewById(R.id.searchlist);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //      performSearch();
                        return true;
                    }
                    return false;
                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
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


            // prepare elements to display
            ArrayList<Item> items = Item.getNewList();


            // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
//        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(getActivity(), items);

            // Call Adapter
            CallAdapter();
        }catch (Exception e){
            Toast.makeText(getContext(), "ListFragment_oncreatview "+e,
                    Toast.LENGTH_SHORT).show();

        }

        return inflatedView;
    }

    public void CallAdapter() {
        try {
            ArrayList<Report> reports=SplashScreen.sql.GetList(category,place,city);
            if(reports!=null) {
                adapter = new FoldingCellListAdapter(getActivity(), reports);

                // set elements to adapter
                if (adapter != null) {
                    theListView.setVisibility(View.VISIBLE);
                    theListView.setAdapter(adapter);


                    // set on click event listener to list view
                    theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            // toggle clicked cell state
                            ((FoldingCell) view).toggle(false);
                            // register in adapter that state for selected cell is toggled
                            adapter.registerToggle(pos);
                        }
                    });
                }
            }
            else{
                theListView.setVisibility(View.GONE);
            }

        }catch (Exception e){
            Toast.makeText(getContext(), "ListFragment_callAdapter "+e,
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i("Tag", "Place: " + place.getName());
                Log.d("Tag", "address" + place.getAddress());
                getLocationFromAddress((String) place.getAddress());
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
    public void getLocationFromAddress(String strAddress) {
        //Create coder with Activity context - this
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5);

            //check for null
            if (address == null) {
                return;
            }

            //Lets take first possibility from the all possibilities.
            Address location = address.get(0);
            place=address.get(0).getAddressLine(0);
            city=address.get(0).getAddressLine(1)  +", "+address.get(0).getAddressLine(2);
            editText.setText(place);
            CallAdapter();

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //  LatLng loc1 = new LatLng(24.831343,67.156085);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        Resources resources = new Resources();

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            try {


            if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[0])) {
                category=resources.name[0][0];
                CallAdapter();

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[1])) {
                category=resources.name[1][0];
                CallAdapter();

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[2])) {
                category=resources.name[2][0];
                CallAdapter();

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[3])) {
                category=resources.name[3][0];
                CallAdapter();

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[4])) {
                category=resources.name[4][0];
                CallAdapter();
            } else {
                category="";
                CallAdapter();


            }}catch (Exception e){
                Toast.makeText(getContext(), "ListFragment_onselectmethode "+e,
                        Toast.LENGTH_SHORT).show();

            }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}

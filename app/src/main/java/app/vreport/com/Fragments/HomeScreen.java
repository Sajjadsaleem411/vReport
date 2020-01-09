package app.vreport.com.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.R;

public class HomeScreen extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the circular_progress_bar for this fragment
        View rootView = inflater.inflate(R.layout.activity_home_screen, container, false);
        /*Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);*/
        ViewPager vpPager = (ViewPager) rootView.findViewById(R.id.vpPager);
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getFragmentManager());
        vpPager.setOffscreenPageLimit(3);

        vpPager.setAdapter(adapterViewPager);

        return rootView;
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;
        String[] title = {"MAP", "EVENTS","GRAPH"};

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return MapFragment.newInstance(0, "MAP");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    if(SplashScreen.sql.GetList("","","")!=null) {
                        return MainListFragment.newInstance();
                    }
                    else {
                        return NullFragment.newInstance();
                    }


                case 2: // Fragment # 0 - This will show FirstFragment
                    return GraphFragment.newInstance();

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }
}

package com.example.lecturer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Musee on 2/7/2017.
 */
public class TimetableFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate (R.layout.timetable,container,false);
        return v;
    }

}


//        MonTTFragment fragment = new MonTTFragment();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame2, fragment);
//        fragmentTransaction.commit();

        //Nimeanza hapa pia.............................
//        TextView tv = (TextView) v.findViewById(R.id.textView);
//        tv.setText(getArguments().getString("msg"));
        //Mpaka hapa........................




  //  }

    //..................................................................................
//    public static TimetableFragment newInstance(String text){
//        TimetableFragment f= new TimetableFragment();
//        Bundle b = new Bundle();
//        b.putString("msg", text);
//
//        f.setArguments(b);
//
//        return f;
//
//    }
//}

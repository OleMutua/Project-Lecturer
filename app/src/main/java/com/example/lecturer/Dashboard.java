package com.example.lecturer;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.support.v4.view.PagerAdapter;
import android.widget.Button;


import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lecturer.m_Firebase.FirebaseHelper;
import com.example.lecturer.m_Model.Spacecraft;
import com.example.lecturer.m_UI.CustomAdapter;
import android.widget.ListAdapter;


public class Dashboard extends AppCompatActivity {

    DatabaseReference db;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv;
    EditText codeEditTxt, nameEditTxt, propTxt, descTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.lv);
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);
        //ADAPTER
        adapter = new CustomAdapter(this, helper.retrieve());
//        lv.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });

//        Button btn = (Button) findViewById(R.id.button2);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Dashboard.this, TT.class));
//            }
//        });



        //Set the timetable fragment as initial
        TimetableFragment fragment = new TimetableFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });




        //nimeanza hapa
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        viewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(TimetableFragment.newInstance("Monday"), "Tab 1");
//        adapter.addFragment(TimetableFragment.newInstance("Tuesday"), "Tab 2");
//        adapter.addFragment(TimetableFragment.newInstance("Wednesday"), "Tab 3");
//        viewPager.setAdapter(adapter);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.common_google_signin_btn_icon_dark);
//        //continue for the rest
//




        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });
    }

    private void displayInputDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.timetable_input);
        codeEditTxt = (EditText) d.findViewById(R.id.unitcode);
        nameEditTxt = (EditText) d.findViewById(R.id.unitname);
        propTxt = (EditText) d.findViewById(R.id.start);
        descTxt = (EditText) d.findViewById(R.id.end);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String code = codeEditTxt.getText().toString();
                String name = nameEditTxt.getText().toString();
                String propellant = propTxt.getText().toString();
                String desc = descTxt.getText().toString();
                //SET DATA
                Spacecraft s = new Spacecraft();
                s.setCode(code);
                s.setName(name);
                s.setPropellant(propellant);
                s.setDescription(desc);
                //SIMPLE VALIDATION
                if (name != null && name.length() > 0) {
                    //THEN SAVE

                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        codeEditTxt.setText("");
                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");

                        adapter = new CustomAdapter(Dashboard.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Dashboard.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }

    public void selectFragment(MenuItem tag) {
        Fragment fragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (tag.getItemId()){
            case R.id.Timetable:
                TimetableFragment a = new TimetableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,a).commit();
                break;

            case R.id.Assignments:
                AssignmentsFragment b = new AssignmentsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,b).commit();
                break;

            case R.id.Submitted:
                SubmittedFragment c = new SubmittedFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,c).commit();
                break;

            case R.id.Attendance:
                ClassAttendanceFragment d = new ClassAttendanceFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,d).commit();
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.signout) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
//
//    class ViewPagerAdapter extends FragmentPagerAdapter{
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) (super(manager);)
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.size();
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title){
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            //return super.getPageTitle(position);
//            return (CharSequence) mFragmentList.get(position);
//        }
//
//        @Override
//
//        public boolean onCreateOptionsMenu(Menu menu) {
//            getMenuInflater().inflate(R.menu.menu_bottom_navigation, menu);
//            return true;
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id=item.getItemId();
//
//            if (id == R.id.action_music) {
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//   }
//
//}
package com.example.lecturer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lecturer.Dashboard;
import com.example.lecturer.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lecturer.m_Firebase.FirebaseHelper;
import com.example.lecturer.m_Model.Spacecraft;
import com.example.lecturer.m_UI.CustomAdapter;
/*
1.INITIALIZE FIREBASE DB
2.INITIALIZE UI
3.DATA INPUT
 */
public class Main2Activity extends AppCompatActivity {

    DatabaseReference db;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv;
    EditText codeEditTxt, nameEditTxt, propTxt, descTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    }
    //DISPLAY INPUT DIALOG
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
                String name = nameEditTxt.getText().toString();
                String propellant = propTxt.getText().toString();
                String desc = descTxt.getText().toString();
                //SET DATA
                Spacecraft s = new Spacecraft();
                s.setName(name);
                s.setPropellant(propellant);
                s.setDescription(desc);
                //SIMPLE VALIDATION
                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");
                        adapter = new CustomAdapter(Main2Activity.this, helper.retrieve());
                        //lv.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Main2Activity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }
}
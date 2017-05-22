package com.example.lecturer;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Musee on 2/10/2017.
 */

public class TT extends AppCompatActivity {

    //we will use these constants later to pass the unit name and id to another activity
    public static final String UNIT_NAME = "com.example.lecturer.unitName";
    public static final String UNIT_ID = "com.example.lecturer.unitId";

    //view objects
    EditText editTextName;
    Spinner spinnerGenre; //Genre=unitcode
    Spinner spinnerDay;
    Button buttonAddUnit;
    ListView listViewUnits;

    //a list to store all the units from firebase database
    List<Unit> units;

    //our database reference object
    DatabaseReference databaseUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_timetable);

        //getting the reference of units node
        databaseUnits = FirebaseDatabase.getInstance().getReference("units");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerGenre = (Spinner) findViewById(R.id.spinnerGenres);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDays);
        listViewUnits = (ListView) findViewById(R.id.listViewUnits);

        buttonAddUnit = (Button) findViewById(R.id.buttonAddUnit);

        //list to store units
        units = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addUnit()
                //the method is defined below
                //this method is actually performing the write operation
                addUnit();
            }
        });

//        //attaching listener to listview
//        listViewUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //getting the selected artist
//                Unit unit = units.get(i);
//
//                //creating an intent
//                Intent intent = new Intent(getApplicationContext(), UnitActivity.class);
//
//                //putting artist name and id to intent
//                intent.putExtra(UNIT_ID, unit.getUnitId());
//                intent.putExtra(UNIT_NAME, unit.getUnitName());
//
//                //starting the activity with intent
//                startActivity(intent);
//            }
//        });

        listViewUnits.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = units.get(i);
                showUpdateDeleteDialog(unit.getUnitId(), unit.getUnitName());
                return true;
            }
        });
    }

    private void showUpdateDeleteDialog(final String unitId, String unitName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerGenre = (Spinner) dialogView.findViewById(R.id.spinnerGenres);
        final Spinner spinnerDay = (Spinner) dialogView.findViewById(R.id.spinnerDays);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUnit);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUnit);

        dialogBuilder.setTitle(unitName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenre.getSelectedItem().toString();
                String day = spinnerDay.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateUnit(unitId, name, genre, day);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteUnit(unitId);
                b.dismiss();
            }
        });
    }

    private boolean updateUnit(String id, String name, String genre, String day) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("units").child(id);

        //updating unit
        Unit unit = new Unit(id, name, genre, day);
        dR.setValue(unit);
        Toast.makeText(getApplicationContext(), "Unit Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUnit(String id) {
        //getting the specified unit reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("units").child(id);

        //removing unit
        dR.removeValue();

        //getting the tracks reference for the specified artist
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        //removing all tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Unit Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseUnits.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                units.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Unit unit = postSnapshot.getValue(Unit.class);
                    //adding artist to the list
                    units.add(unit);
                }

                //creating adapter
                UnitList unitAdapter = new UnitList(TT.this, units);
                //attaching adapter to the listview
                listViewUnits.setAdapter(unitAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    * This method is saving a new unit to the
    * Firebase Realtime Database
    * */
    private void addUnit() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our unit
            String id = databaseUnits.push().getKey();

            //creating a Unit Object
            Unit unit = new Unit(id, name, genre, day);

            //Saving the Unit
            databaseUnits.child(id).setValue(unit);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Unit added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
package com.example.lecturer.m_UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lecturer.R;
import com.example.lecturer.m_Model.Spacecraft;
import java.util.ArrayList;

/**
 * Created by Musee on 2/11/2017.
 */

public class CustomAdapter extends BaseAdapter{
    Context c;
    ArrayList<Spacecraft> spacecrafts;
    public CustomAdapter(Context c, ArrayList<Spacecraft> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }
    @Override
    public int getCount() {
        return spacecrafts.size();
    }
    @Override
    public Object getItem(int position) {
        return spacecrafts.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        }
        TextView codeTxt= (TextView) convertView.findViewById(R.id.codeTxt);
        TextView nameTxt= (TextView) convertView.findViewById(R.id.nameTxt);
        TextView propTxt= (TextView) convertView.findViewById(R.id.starttimeTxt);
        TextView descTxt= (TextView) convertView.findViewById(R.id.endtimeTxt);
        final Spacecraft s= (Spacecraft) this.getItem(position);
        codeTxt.setText(s.getCode());
        nameTxt.setText(s.getName());
        propTxt.setText(s.getPropellant());
        descTxt.setText(s.getDescription());
        //ONITECLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,s.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
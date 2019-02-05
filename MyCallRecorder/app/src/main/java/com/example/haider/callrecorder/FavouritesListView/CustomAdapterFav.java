package com.example.haider.callrecorder.FavouritesListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haider.callrecorder.Contact;
import com.example.haider.callrecorder.R;

import java.util.List;

public class CustomAdapterFav  extends ArrayAdapter<Contact> {

    Context c;
    int layoutFile;
    private List<Contact> arraylist;

    public CustomAdapterFav(Context c, int layoutFile, List<Contact> data) {
        super(c, layoutFile, data);
        this.c = c;
        this.layoutFile = layoutFile;
        this.arraylist = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_contacts, parent, false);
        }

        TextView rowId = (TextView) convertView.findViewById(R.id.row_id);
        rowId.setText(String.valueOf(arraylist.get(position).getID()));

        TextView txt = (TextView) convertView.findViewById(R.id.row_name);
        txt.setText(arraylist.get(position).getName());

        TextView txt2 = (TextView) convertView.findViewById(R.id.row_phoneNumber);
        txt2.setText(arraylist.get(position).getPhoneNumber());

        TextView txt3 = (TextView) convertView.findViewById(R.id.row_date);
        String date = arraylist.get(position).getDate();
        date = date.substring(0, date.indexOf("GMT")-4);
        txt3.setText(date);

        ImageView incoming_outgoing = (ImageView) convertView.findViewById(R.id.incoming_outgoing);
        String incom_outgo = arraylist.get(position).getIsIncoming();
        if (incom_outgo.equalsIgnoreCase("true")) {
            incoming_outgoing.setImageResource(R.drawable.incoming);
        } else {
            incoming_outgoing.setImageResource(R.drawable.outgoing);
        }

        return convertView;


    }
}

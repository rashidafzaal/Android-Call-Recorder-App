package com.example.haider.callrecorder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haider on 10/25/2018.
 */

public class CustomAdapter extends ArrayAdapter<Contact> implements Filterable {
    Context c;
    int layoutFile;
    private List<Contact> arraylist;
    private List<Contact> origData;

    public CustomAdapter(Context c, int layoutFile, List<Contact> data) {
        super(c, layoutFile, data);
        this.c = c;
        this.layoutFile = layoutFile;
        this.arraylist = data;
        this.origData = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_contacts, parent, false);
        }

        TextView rowId = (TextView) convertView.findViewById(R.id.row_id);
        rowId.setText(String.valueOf(arraylist.get(position).getID()));

        TextView path = (TextView) convertView.findViewById(R.id.row_path);
        path.setText(String.valueOf(arraylist.get(position).getPath()));

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


    @Override
    public Filter getFilter(){
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null && constraint.toString().length() > 0) {
                    List<Contact> founded = new ArrayList<Contact>();
                    for(Contact item: origData){
                        if(item.getName().toString().toLowerCase().contains(constraint)){
                            founded.add(item);
                        }
                    }

                    result.values = founded;
                    result.count = founded.size();
                }else {
                    result.values = origData;
                    result.count = origData.size();
                }
                return result;


            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                for (Contact item : (List<Contact>) results.values) {
                    add(item);
                }
                notifyDataSetChanged();

            }

        };
    }

    //==============


//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint,FilterResults results) {
//
//                arraylist = (ArrayList<Contact>) results.values; // has the filtered values
////                notifyDataSetChanged();  // notifies the data with new filtered values
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                ArrayList<Contact> FilteredArrList = new ArrayList<Contact>();
//
//                if (origData == null) {
//                    origData = new ArrayList<Contact>(arraylist); // saves the original data in mOriginalValues
//                }
//
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = origData.size();
//                    results.values = origData;
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < origData.size(); i++) {
//                        String data = origData.get(i).getName();
//                        if (data.toLowerCase().contains(constraint.toString())) {
//                            FilteredArrList.add(origData.get(i));
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//                }
//                return results;
//            }
//        };
//        return filter;
//    }
}
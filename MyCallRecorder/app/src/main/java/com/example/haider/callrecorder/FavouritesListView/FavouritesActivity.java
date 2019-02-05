package com.example.haider.callrecorder.FavouritesListView;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.haider.callrecorder.Contact;
import com.example.haider.callrecorder.DatabaseHandler;
import com.example.haider.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    Contact[] array;
    CustomAdapterFav adapter;
    ListView lv;

    SharedPreferences prefs;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);


        db = new DatabaseHandler(FavouritesActivity.this);
        List<Contact> contacts = db.getAllContacts();

        List<Contact> favContacts = new ArrayList<>();
        for (int count = 0 ; count < contacts.size(); count++)
        {
            if (contacts.get(count).getFavourite().equalsIgnoreCase("yes")) {
                favContacts.add(contacts.get(count));
            }
        }

        if (favContacts.size() != 0)
        {
            adapter = new CustomAdapterFav(this, R.layout.row_contacts, favContacts);
            lv = (ListView) findViewById(R.id.favListView);
            lv.setAdapter(adapter);
        }

    }
}

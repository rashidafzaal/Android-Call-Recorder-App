package com.example.haider.callrecorder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        editor = prefs.edit();

        CheckBox checkBox = (CheckBox) findViewById(R.id.allowToDelete);

        String found = prefs.getString("myDeletePermission", "");
        if (found.equalsIgnoreCase("true"))
        {
            checkBox.setChecked(true);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!isChecked)
                {
                    editor.putString("myDeletePermission", "false");
                    editor.commit();
                    Toast.makeText(SettingsActivity.this, "You can't Delete Calls now", Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putString("myDeletePermission", "true");
                    editor.commit();
                    Toast.makeText(SettingsActivity.this, "You can Delete calls now", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

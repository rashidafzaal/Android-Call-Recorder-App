package com.example.haider.callrecorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haider.callrecorder.FavouritesListView.FavouritesActivity;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemLongClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener, View.OnClickListener {

    Contact[] array;
    CustomAdapter adapter;
    ListView lv;

    DatabaseHandler db;
    List<Contact> contacts;

    MediaPlayer mediaPlayer;
    private SeekBar mSeekbar;
    private ImageView playPauseBtn;

    boolean found = true;


    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = prefs.edit();

        db = new DatabaseHandler(MainActivity.this);
        contacts = db.getAllContacts();

        if (contacts.size() != 0) {
            adapter = new CustomAdapter(this, R.layout.row_contacts, contacts);
            lv = (ListView) findViewById(R.id.mListView);
            lv.setAdapter(adapter);
            lv.setOnItemLongClickListener(this);
            lv.setOnItemClickListener(this);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get Views
        playPauseBtn = (ImageView) findViewById(R.id.playPauseBtn);
        mSeekbar = (SeekBar) findViewById(R.id.mSeekbar);
        //set Listeners
        mSeekbar.setOnSeekBarChangeListener(this);
        playPauseBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        //remove the cross button
        ImageView closeBtn = (ImageView) searchViewActionBar.findViewById(R.id.search_close_btn);
        closeBtn.setEnabled(false);
        closeBtn.setImageDrawable(null);
        searchViewActionBar.setIconifiedByDefault(false);

        searchViewActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (contacts.size() != 0) {
                    adapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });

        //close search bar and update listview
        searchViewActionBar.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View arg0) {
                // search was detached/closed
                contacts = db.getAllContacts();
                if (contacts.size() != 0) {
                    adapter = new CustomAdapter(MainActivity.this, R.layout.row_contacts, contacts);
                    lv = (ListView) findViewById(R.id.mListView);
                    lv.setAdapter(adapter);
                    lv.setOnItemLongClickListener(MainActivity.this);
                    lv.setOnItemClickListener(MainActivity.this);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                // search was open
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_By_Name) {

            if (contacts.size() != 0) {
                Collections.sort(contacts, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact contact, Contact t1) {
                        return (int) contact.getName().charAt(0) - (int) t1.getName().charAt(0);
                    }
                });
                adapter.notifyDataSetChanged();

            }
            return true;
        }
        if (id == R.id.action_sort_By_Date) {

            if (contacts.size() != 0) {
                Collections.sort(contacts, new CustomComparator());
                adapter.notifyDataSetChanged();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class CustomComparator implements Comparator<Contact> {

        @Override
        public int compare(Contact contact, Contact t1) {
            return contact.getDate().compareTo(t1.getDate());
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); //don't push on stack
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); //don't push on stack
            startActivity(intent);

        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Download my App Android Call Recorder");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share via"));

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        playPauseBtn.setImageResource(R.drawable.pause);
        found = true;

        //get AudioPath
        TextView path = (TextView) view.findViewById(R.id.row_path);
        String audioPath = path.getText().toString();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setFileName
        //String filename = audioPath.substring(audioPath.lastIndexOf("/") + 1);
        TextView nameToBeSet = view.findViewById(R.id.row_name);
        TextView filenameTxtView = findViewById(R.id.filenameTxtView);
        filenameTxtView.setText(nameToBeSet.getText().toString());

        //set Duration
        TextView durationTxtView = findViewById(R.id.durationTxtView);
        durationTxtView.setText(getDurationInSeconds());

        mSeekbar.setMax(mediaPlayer.getDuration());
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        mediaPlayer.start();
    }

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                try {

                    mSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                } catch (IllegalStateException e) {

                }
            }
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.playPauseBtn:

                int currentPostion = 0;
                //paused
                if (found) {
                    if (contacts.size() != 0) {
                        if (mediaPlayer != null) {
                            currentPostion = mediaPlayer.getCurrentPosition();
                            playPauseBtn.setImageResource(R.drawable.play);
                            mediaPlayer.seekTo(currentPostion);
                            //mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                            mediaPlayer.pause();
                            found = false;

                        } else {
                            Toast.makeText(MainActivity.this, "No Call Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //playing
                else {
                    if (contacts.size() != 0) {
                        playPauseBtn.setImageResource(R.drawable.pause);
                        mediaPlayer.seekTo(currentPostion);
                        mediaPlayer.start();
                        found = true;
                    }
                }

                break;
            default:
                break;
        }
    }


    private String getDurationInSeconds() {

        int duration = mediaPlayer.getDuration();
        String time = String.format("%02d:" +
                        "%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        return time;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean found) {
        if (mediaPlayer != null && found) {
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView id = (TextView) view.findViewById(R.id.row_id);
        TextView Name = view.findViewById(R.id.row_name);
        String rowId = id.getText().toString();
        String currentName = Name.getText().toString();

        OpenMyDialog(rowId, currentName);
        return false;
    }

    //==================================== OpenMyDialog Mehtod=========================================

    public void OpenMyDialog(final String rowId, final String currentName) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup) findViewById(R.id.mydialog));

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        TextView title = new TextView(this);
        title.setText("Choose Option");
        title.setPadding(40, 40, 40, 40);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        builder.setCustomTitle(title);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        Button fav = (Button) layout.findViewById(R.id.favorite);
        Button del = (Button) layout.findViewById(R.id.delete);
        Button ren = (Button) layout.findViewById(R.id.rename);
        final EditText ed = (EditText) layout.findViewById(R.id.newName);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Contact contact = new Contact();
                contact.setID(Integer.parseInt(rowId));
                db.updateContact(contact);
                Toast.makeText(MainActivity.this, "Favourited", Toast.LENGTH_SHORT).show();

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String found = prefs.getString("myDeletePermission", "");
                if (found.equalsIgnoreCase("true"))
                {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(rowId));
                    db.deleteContact(contact);
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please Allow in Settings to Delete Calls", Toast.LENGTH_SHORT).show();
                }
                

            }
        });

        ren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = ed.getText().toString();
                newName = currentName +" - "+newName;

                if (!TextUtils.isEmpty(newName)) {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(rowId));
                    contact.setName(newName);
                    db.updateRename(contact);
                    Toast.makeText(MainActivity.this, "Note Added.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}

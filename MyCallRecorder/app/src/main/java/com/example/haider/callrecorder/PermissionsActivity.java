package com.example.haider.callrecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PermissionsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_PROCESS_OUTGOING = 2;
    private static final int MY_PERMISSIONS_READ_CONTACTS = 3;
    private static final int MY_PERMISSIONS_WRITE_CONTACTS = 4;
    private static final int MY_PERMISSIONS_READ_CALL_LOGS = 5;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 6;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 7;
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isReadPhoneStatePermission() && isReadContacts() && isWriteExternalStorage() && isRecordAudio() && isReadExternalStorage()
                && isProcessOutgoingPermission() && isWriteContats() && isReadCallLogs()) {
            Intent intent = new Intent(PermissionsActivity.this, MainActivity.class);
            //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); //don't push on stack
            startActivity(intent);
        } else {

        }

    }

    public void givePermissions(View view) {
        if (isReadPhoneStatePermission() && isReadContacts() && isWriteExternalStorage() && isRecordAudio() && isReadExternalStorage()
                && isProcessOutgoingPermission() && isWriteContats() && isReadCallLogs()) {
            Intent intent = new Intent(PermissionsActivity.this, MainActivity.class);
            //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); //don't push on stack

            startActivity(intent);
        } else {
        }
    }

    private boolean isRecordAudio(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private boolean isWriteExternalStorage(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    private boolean isReadExternalStorage(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    private boolean isReadCallLogs() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.READ_CALL_LOG},
                        MY_PERMISSIONS_READ_CALL_LOGS);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private boolean isReadContacts() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_READ_CONTACTS);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private boolean isWriteContats() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSIONS_WRITE_CONTACTS);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private boolean isProcessOutgoingPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                        MY_PERMISSIONS_PROCESS_OUTGOING);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private boolean isReadPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_READ_PHONE_STATE);

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isReadContacts();
            } else {
                Toast.makeText(this, "Give All Permissions", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == MY_PERMISSIONS_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isWriteExternalStorage();
            } else {
                Toast.makeText(this, "Give All Permissions", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isRecordAudio();
            } else {
                Toast.makeText(this, "Give All Permissions", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == MY_PERMISSIONS_RECORD_AUDIO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isProcessOutgoingPermission();
            } else {
                Toast.makeText(this, "Give All Permissions", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == MY_PERMISSIONS_PROCESS_OUTGOING) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isWriteContats();
                Toast.makeText(this, "Calls Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
            }
        }


        if (requestCode == MY_PERMISSIONS_WRITE_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isReadCallLogs();
                Toast.makeText(this, "Write Contacts permission Granted", Toast.LENGTH_SHORT).show();
            } else {
            }
        }


        if (requestCode == MY_PERMISSIONS_READ_CALL_LOGS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Calls Permissions Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PermissionsActivity.this, MainActivity.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); //don't push on stack
                startActivity(intent);
            } else {
            }
        }

    }
}

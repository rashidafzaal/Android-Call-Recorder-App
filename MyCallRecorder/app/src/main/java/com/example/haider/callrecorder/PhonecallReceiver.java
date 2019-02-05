package com.example.haider.callrecorder;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by haider on 9/22/2018.
 */

public abstract class PhonecallReceiver extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing
    String name;

    MediaRecorder recorder;
    TelephonyManager telManager;
    boolean recordStarted;
    Context contxt;
    File audiofile = null;


    @Override
    public void onReceive(Context context, Intent intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String incomingCallerNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String incomingCallerName = getContactDisplayNameByNumber(incomingCallerNumber, context);

            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, incomingCallerNumber, incomingCallerName, intent);
        }
    }

    private void startRecording(Context context, Intent intent) {

        this.contxt = context;
        recorder = new MediaRecorder();
        String action = intent.getAction();

        try {
            File sampleDir = Environment.getExternalStorageDirectory().getAbsoluteFile();
            try {
                audiofile = File.createTempFile("mysound" + System.currentTimeMillis(), ".3gp", sampleDir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            recordStarted = true;
            telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private final PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        break;
                    }
                    case TelephonyManager.CALL_STATE_IDLE: {
                        if (recordStarted) {
                            recorder.stop();
                            recordStarted = false;
                        }
                        break;
                    }
                    default: {
                    }
                }
            } catch (Exception ex) {

            }
        }
    };


    protected void onIncomingCallStarted(Context ctx, String number, Date start, String name, boolean isIncoming, String path) {
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start, String name, boolean isIncoming, String path) {
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
    }

    public String getContactDisplayNameByNumber(String number, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        name = "Incoming call from";

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, null, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                // this.id =
                // contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                // String contactId =
                // contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            } else {
                name = "Unknown number";
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }

    public void onCallStateChanged(Context context, int state, String number, String name, Intent intent) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                startRecording(context, intent);
                onIncomingCallStarted(context, number, callStartTime, name, isIncoming, audiofile.getAbsolutePath());
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    startRecording(context, intent);
                    onOutgoingCallStarted(context, savedNumber, callStartTime, name, isIncoming, audiofile.getAbsolutePath());

                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    if (recordStarted) {
                        recorder.stop();
                        recordStarted = false;
                    }
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                } else {
                    if (recordStarted) {
                        recorder.stop();
                        recordStarted = false;
                    }
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }
}

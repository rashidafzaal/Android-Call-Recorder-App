package com.example.haider.callrecorder;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Date;

/**
 * Created by haider on 9/22/2018.
 */
public class CallReceiver extends PhonecallReceiver {

    MediaPlayer mediaPlayer;

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start, String name, boolean isIncoming, String path) {

        DatabaseHandler db = new DatabaseHandler(ctx);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhoneNumber(number);
        contact.setDate(String.valueOf(start));
        contact.setIsIncoming(String.valueOf(isIncoming));
        contact.setFavourite("no");
        contact.setPath(path);
        db.addContact(contact);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start, String name, boolean isIncoming, String path) {
        DatabaseHandler db = new DatabaseHandler(ctx);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhoneNumber(number);
        contact.setDate(String.valueOf(start));
        contact.setIsIncoming(String.valueOf(isIncoming));
        contact.setFavourite("no");
        contact.setPath(path);
        db.addContact(contact);
    }


    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

}
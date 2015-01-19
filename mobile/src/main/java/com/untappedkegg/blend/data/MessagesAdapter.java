package com.untappedkegg.blend.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.TextBasedSmsColumns;
import android.util.Log;

import com.untappedkegg.blend.AppState;
import com.untappedkegg.blend.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kyle on 1/8/15.
 */
/**
 * <p>Basically just look at <b>{@link android.provider.Telephony}</b> for anything </p>
 *
 * <p>Use {@link android.provider.Telephony.TextBasedSmsColumns} for ColumnNames </p>
 * <p>Use {@link android.provider.Telephony.MmsSms} for MmsSms Uri's </p>
 */
public class MessagesAdapter {
    /*----- CONSTANTS -----*/
    final static String LOG_TAG = MessagesAdapter.class.getSimpleName();



//    public static final int adapter = MmsSms.CONTENT_CONVERSATIONS_URI;

    public static final String QUEUERY_INBOX = "content://sms/inbox/";
    public static final String QUEUERY_FAILED = "content://sms/failed/";
    public static final String QUEUERY_QUEUED = "content://sms/queued/";
    public static final String QUEUERY_SENT = "content://sms/sent/";
    public static final String QUEUERY_DRAFT = "content://sms/draft/";
    public static final String QUEUERY_OUTBOX = "content://sms/outbox/";
    public static final String QUEUERY_UNDELIVERED = "content://sms/undelivered/";
    public static final String QUEUERY_SMS_ALL = "content://sms/all/";

    /*---- SMS + MMS ----*/

    // Telephony.MmsSms.CONTENT_URI                 = content://mms-sms/
    // Telephony.MmsSms.CONTENT_CONVERSATIONS_URI   = content://mms-sms/conversations
    // Telephony.MmsSms.CONTENT_DRAFT_URI           = content://mms-sms/draft
    // Telephony.MmsSms.CONTENT_LOCKED_URI          = content://mms-sms/locked
    // Telephony.MmsSms.CONTENT_FILTER_BYPHONE_URI  = content://mms-sms/messages/byphone
    // Telephony.MmsSms.SEARCH_URI                  = content://mms-sms/search
    // Telephony.MmsSms.CONTENT_UNDELIVERED_URI     = content://mms-sms/undelivered

    public static class TextMessage {
        /*----- VARIABLES -----*/
        public String name;
        public String snippet;
        public String threadId;
        public String date;
        public Drawable photo;

        public TextMessage(String name, String snippet, String threadId, String date, Drawable photo) {
            this.name = name;
            this.snippet = snippet;
            this.threadId = threadId;
            this.photo = photo;
            this.date = date;
        }
    }

    public static class Conversation {
        public String name;
        public String read;
        public String threadId;
        public String snippet;
        public String date;
        public Drawable photo;

         public Conversation(String name, String read, String threadId, String snippet, String date, Drawable photo)  {
             this.name = name;
             this.read = read;
             this.threadId = threadId;
             this.snippet = snippet;
             this.date = date;
             this.photo = photo;

         }

    }


    private static final Context ctx = AppState.getApplication();

//    public static final Cursor readAllMessages() {
//       return ctx.getContentResolver().query(Uri.parse(QUERY_MESSAGES), null, null, null, "date DESC");
//    }

    public static final Cursor readThreadMessages(String contactId) {
        return ctx.getContentResolver().query(MmsSms.CONTENT_CONVERSATIONS_URI, null, String.format("%s = %s", TextBasedSmsColumns.THREAD_ID, contactId), null, "date DESC");
    }

    public static ArrayList<Conversation> conversationsToArrayList() {


        final Cursor c = ctx.getContentResolver().query(MmsSms.CONTENT_CONVERSATIONS_URI, null, null, null, "date DESC");

        if(c.moveToFirst()) {
            final int count = c.getCount();
            final int threadIdCol = c.getColumnIndex(TextBasedSmsColumns.THREAD_ID);
            final int snippetCol = c.getColumnIndex(TextBasedSmsColumns.BODY);
            final int phoneNumCol = c.getColumnIndex(TextBasedSmsColumns.ADDRESS);
            final int dateCol = c.getColumnIndex(TextBasedSmsColumns.DATE);
            final int readCol = c.getColumnIndex(TextBasedSmsColumns.READ);
            ArrayList<Conversation> mList = new ArrayList<>();

            for (int j = 0; j < count; j++) {
                c.moveToPosition(j);
                final String phoneNumber = c.getString(phoneNumCol);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(c.getLong(dateCol));
//                Log.e(LOG_TAG, String.format("name = %s, Thread_ID = %s, Snippet = %s, date = %s, photo = %s", MessageUtils.getContactName(phoneNumber) , MessageUtils.fetchContactIdFromPhoneNumber(phoneNumber), c.getString(26), formatter.format(calendar.getTime()), MessageUtils.getDrawableFromNumber(phoneNumber).toString()));
                mList.add(new Conversation(MessageUtils.getContactName(phoneNumber) ,c.getString(readCol), c.getString(threadIdCol), c.getString(snippetCol), formatter.format(calendar.getTime()), MessageUtils.getDrawableFromNumber(phoneNumber)));

            }
            c.close();
            return mList;

        } else {
            c.close();
            Log.e("Error", "Cursor is null or empty");
        }
            return null;

    }

    public static ArrayList<TextMessage> threadToArrayList(String threadId) {


        final Cursor c = ctx.getContentResolver().query(MmsSms.CONTENT_CONVERSATIONS_URI, null, String.format("%s = %s", TextBasedSmsColumns.THREAD_ID, threadId), null, "date DESC");

        if(c.moveToFirst()) {
            final int count = c.getCount();
MessageUtils.printMessagesToLog(c, false);
            final int threadIdCol = c.getColumnIndex(TextBasedSmsColumns.THREAD_ID);
            final int snippetCol = c.getColumnIndex(TextBasedSmsColumns.BODY);
            final int phoneNumCol = c.getColumnIndex(TextBasedSmsColumns.ADDRESS);
            final int dateCol = c.getColumnIndex(TextBasedSmsColumns.DATE);
            final int readCol = c.getColumnIndex(TextBasedSmsColumns.READ);
            ArrayList<TextMessage> mList = new ArrayList<>();

            for (int j = 0; j < count; j++) {
                c.moveToPosition(j);
                final String phoneNumber = c.getString(phoneNumCol);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(c.getLong(dateCol));
//                Log.e(LOG_TAG, String.format("name = %s, Thread_ID = %s, Snippet = %s, date = %s, photo = %s", MessageUtils.getContactName(phoneNumber) , MessageUtils.fetchContactIdFromPhoneNumber(phoneNumber), c.getString(26), formatter.format(calendar.getTime()), MessageUtils.getDrawableFromNumber(phoneNumber).toString()));
                mList.add(new TextMessage(MessageUtils.getContactName(phoneNumber), c.getString(snippetCol), c.getString(threadIdCol), formatter.format(calendar.getTime()),  MessageUtils.getDrawableFromNumber(phoneNumber)));
Log.e("test", c.getString(snippetCol));
            }
            c.close();
            return mList;

        } else {
            c.close();
            Log.e("Error", "Cursor is null or empty");
        }
        return null;

    }

}

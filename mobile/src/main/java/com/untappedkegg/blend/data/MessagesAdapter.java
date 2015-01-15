package com.untappedkegg.blend.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.untappedkegg.blend.AppState;
import com.untappedkegg.blend.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kyle on 1/8/15.
 */
public class MessagesAdapter {
    /*----- CONSTANTS -----*/
    final static String LOG_TAG = MessagesAdapter.class.getSimpleName();

    public static final String QUEUERY_INBOX = "content://sms/inbox/";
    public static final String QUEUERY_FAILED = "content://sms/failed/";
    public static final String QUEUERY_QUEUED = "content://sms/queued/";
    public static final String QUEUERY_SENT = "content://sms/sent/";
    public static final String QUEUERY_DRAFT = "content://sms/draft/";
    public static final String QUEUERY_OUTBOX = "content://sms/outbox/";
    public static final String QUEUERY_UNDELIVERED = "content://sms/undelivered/";
    public static final String QUEUERY_SMS_ALL = "content://sms/all/";
    public static final String QUERY_CONVERSATIONS = "content://mms-sms/conversations/";

        public static final String FROM_ADDRESS = "address";
        public static final String DATE_IN_MILLIS = "date";
        public static final String THREAD_ID = "thread_id";
        public static final String SNIPPET = "body";
        public static final String PERSON_ID = "person";

    public static final String QUERY_MESSAGES = "content://sms/";
    public static final String QUEUERY_CONVERSATIONS = "content://sms/conversations/";

        public static final String CONVERSATION_MSG_COUNT = "msg_count"; // col: 0
        public static final String CONVERSATION_THREAD_ID = "thread_id"; // col: 1
        public static final String CONVERSATION_MSG_SNIPPET = "snippet"; // col 3

    public static class TextMessage {
        /*----- VARIABLES -----*/
        public static String name;
        public static String phoneNumber;
        public static String snippet;
        public static String threadId;
        public static Uri photoUri;

        public TextMessage(String name, String phoneNumber, String snippet, String threadId, Uri photoUri) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.snippet = snippet;
            this.threadId = threadId;
            this.photoUri = photoUri;
        }
    }

    public static class Conversation {
        public String name;
        public String messageCount;
        public String threadId;
        public String snippet;
        public String date;
        public Drawable photo;

         public Conversation(String name, String msgCount, String threadId, String snippet, String date, Drawable photo)  {
             this.name = name;
             this.messageCount = msgCount;
             this.threadId = threadId;
             this.snippet = snippet;
             this.date = date;
             this.photo = photo;

         }

//        public static String getName() {return name;}

    }


    private static final Context ctx = AppState.getApplication();

    public static final Cursor readAllMessages() {
       return ctx.getContentResolver().query(Uri.parse(QUERY_MESSAGES), null, null, null, "date DESC");
    }

    public static final Cursor readThreadMessages(String contactId) {
        return ctx.getContentResolver().query(Uri.parse(QUERY_CONVERSATIONS), null, String.format("%s = %s", THREAD_ID, contactId), null, "date DESC");
    }

    public static ArrayList<Conversation> conversationsToArrayList() {


        final Cursor c = ctx.getContentResolver().query(Uri.parse(QUERY_CONVERSATIONS), null, null, null, "date DESC");
//MessageUtils.printMessagesToLog(c,false);
        if(c.moveToFirst()) {
            final int count = c.getCount();

//            final int personIdCol = c.getColumnIndex(PERSON_ID);
            final int threadIdCol = c.getColumnIndex(THREAD_ID);
            final int snippetCol = c.getColumnIndex(SNIPPET);
            final int phoneNumCol = c.getColumnIndex(FROM_ADDRESS);
            final int dateCol = c.getColumnIndex(DATE_IN_MILLIS);
            ArrayList<Conversation> mList = new ArrayList<>();

            for (int j = 0; j < count; j++) {
                c.moveToPosition(j);
                final String phoneNumber = c.getString(phoneNumCol);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(c.getLong(dateCol));
//                Log.e(LOG_TAG, String.format("name = %s, Thread_ID = %s, Snippet = %s, date = %s, photo = %s", MessageUtils.getContactName(phoneNumber) , MessageUtils.fetchContactIdFromPhoneNumber(phoneNumber), c.getString(26), formatter.format(calendar.getTime()), MessageUtils.getDrawableFromNumber(phoneNumber).toString()));
                mList.add(new Conversation(MessageUtils.getContactName(phoneNumber) ,null, c.getString(threadIdCol), c.getString(snippetCol), formatter.format(calendar.getTime()), MessageUtils.getDrawableFromNumber(phoneNumber)));

            }
            c.close();
            return mList;

        } else {
            Log.e("Error", "Cursor is null or empty");
        }
            return null;

    }

}

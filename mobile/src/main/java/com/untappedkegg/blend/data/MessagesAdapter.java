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
    public static final String QUEUERY_INBOX = "content://sms/inbox/";
    public static final String QUEUERY_FAILED = "content://sms/failed/";
    public static final String QUEUERY_QUEUED = "content://sms/queued/";
    public static final String QUEUERY_SENT = "content://sms/sent/";
    public static final String QUEUERY_DRAFT = "content://sms/draft/";
    public static final String QUEUERY_OUTBOX = "content://sms/outbox/";
    public static final String QUEUERY_UNDELIVERED = "content://sms/undelivered/";
    public static final String QUEUERY_SMS_ALL = "content://sms/all/";
    public static final String QUERY_ALL = "content://mms-sms/conversations/";
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
        public static String name;
        public static String messageCount;
        public static String threadId;
        public static String snippet;
        public static String date;
        public static Drawable photo;

         public Conversation(String name, String msgCount, String threadId, String snippet, String date, Drawable photo)  {
             this.name = name;
             this.messageCount = msgCount;
             this.threadId = threadId;
             this.snippet = snippet;
             this.date = date;
             this.photo = photo;

         }

    }


    private static final Context ctx = AppState.getApplication();

    public static final Cursor readAllMessages() {
       return ctx.getContentResolver().query(Uri.parse(QUERY_ALL), null, null, null, "date DESC");

    }

    public static ArrayList conversationsToArrayList() {


        final Cursor c = ctx.getContentResolver().query(Uri.parse(QUERY_ALL), null, null, null, "date DESC");

        if(c.moveToFirst()) {
            final String[] colNames = c.getColumnNames();
            final int colCount = c.getColumnCount();
            final int count = c.getCount();
            ArrayList<Conversation> mList = new ArrayList<>(count);

            for (int j = 0; j < count; j++) {
                c.moveToPosition(j);
                final String phoneNumber = c.getString(18);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(c.getLong(25));

                mList.add(new Conversation(MessageUtils.getContactName(phoneNumber) ,null, MessageUtils.fetchContactIdFromPhoneNumber(phoneNumber), c.getString(26), formatter.format(calendar.getTime()), MessageUtils.getDrawableFromNumber(phoneNumber)));

            }
            c.close();
            return mList;

        } else {
            Log.e("Error", "Cursor is null or empty");
        }
            return null;

    }

}

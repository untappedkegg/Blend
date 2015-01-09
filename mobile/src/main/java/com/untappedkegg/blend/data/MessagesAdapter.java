package com.untappedkegg.blend.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.untappedkegg.blend.AppState;

/**
 * Created by kyle on 1/8/15.
 */
public class MessagesAdapter {
    /*----- CONSTANTS -----*/
    public static final String INBOX = "content://sms/inbox/";
    public static final String FAILED = "content://sms/failed/";
    public static final String QUEUED = "content://sms/queued/";
    public static final String SENT = "content://sms/sent/";
    public static final String DRAFT = "content://sms/draft/";
    public static final String OUTBOX = "content://sms/outbox/";
    public static final String UNDELIVERED = "content://sms/undelivered/";
    public static final String SMS_ALL = "content://sms/all/";
    public static final String ALL = "content://mms-sms/conversations/";
    public static final String CONVERSATIONS = "content://sms/conversations/";
    //Columns:
    /*
    * 0 = "msg_count"
    * 1 = "thread_id"
    * 2 = "snippet"
    *
    */

    private static final Context ctx = AppState.getApplication();

    public static final Cursor readAllMessages() {
       return ctx.getContentResolver().query(Uri.parse(ALL), null, null, null, "date DESC");

    }

}

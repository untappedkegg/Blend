package com.untappedkegg.blend.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import com.untappedkegg.blend.AppState;
import com.untappedkegg.blend.MmsConfig;
import com.untappedkegg.blend.R;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kyle on 1/7/15.
 */
public final class MessageUtils {
    private static final String LOG_TAG = MessageUtils.class.getSimpleName();



    /**
     * Returns true if the address is an email address
     *
     * @param address the input address to be tested
     * @return true if address is an email address
     */
    public final static boolean isEmailAddress(String address) {
            /*
             * The '@' char isn't a valid char in phone numbers. However, in SMS
             * messages sent by carrier, the originating-address can contain
             * non-dialable alphanumeric chars. For the purpose of thread id
             * grouping, we don't care about those. We only care about the
             * legitmate/dialable phone numbers (which we use the special phone
             * number comparison) and email addresses (which we do straight up
             * string comparison).
             */
        return (!AppState.isNullOrEmpty(address)) && (address.indexOf('@') != -1);
    }


    // An alias (or commonly called "nickname") is:
    // Nickname must begin with a letter.
    // Only letters a-z, numbers 0-9, or . are allowed in Nickname field.
    public final static boolean isAlias(String string) {
        if (!MmsConfig.isAliasEnabled()) {
            return false;
        }

        int len = string == null ? 0 : string.length();

        if (len < MmsConfig.getAliasMinChars() || len > MmsConfig.getAliasMaxChars()) {
            return false;
        }

        if (!Character.isLetter(string.charAt(0))) {    // Nickname begins with a letter
            return false;
        }
        for (int i = 1; i < len; i++) {
            char c = string.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '.')) {
                return false;
            }
        }

        return true;
    }

    public static void printColumnsToLog(Cursor c, boolean closeCursor) {
        if(c.moveToFirst()) {
            final String[] colNames = c.getColumnNames();
            final int colCount = c.getCount();
            Log.e("Index", "Column_Name");
            for(int i = 0; i < colCount; i++) {
                Log.e(String.valueOf(i), String.valueOf(colNames[i]));
            }
        } else {
            Log.e("Error", "Cursor is null or empty");
        }

        if(closeCursor)
            c.close();

    }

    public static void printMessagesToLog(Cursor c, boolean closeCursor) {
        if(c.moveToFirst()) {
            final String[] colNames = c.getColumnNames();
            final int colCount = c.getColumnCount();
            final int count = c.getCount();
            Log.e("Index", "Column_Name");
            for (int j = 0; j < count; j++) {
                Log.e("========== BLEND", "New Message: ==============");
                c.moveToPosition(j);
                for(int i = 0; i < colCount; i++) {
                    Log.e(String.valueOf(i) + " " + String.valueOf(colNames[i]), c.getString(i) + "");
                }

            }


        } else {
            Log.e("Error", "Cursor is null or empty");
        }

        if(closeCursor)
            c.close();

    }


    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = AppState.getApplication().getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static final String fetchContactIdFromPhoneNumber(String phoneNumber) {

        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cFetch = AppState.getApplication().getContentResolver().query(uri,
                new String[]{PhoneLookup.DISPLAY_NAME, PhoneLookup._ID},
                null, null, null);

        String contactId = "";


        if (cFetch.moveToFirst()) {

            cFetch.moveToFirst();

            contactId = cFetch.getString(cFetch
                    .getColumnIndex(PhoneLookup._ID));

        }

//        Log.e(LOG_TAG, "Contact_ID = " + contactId);

        return contactId;

    }

    public static final Uri getPhotoUri(String contactId) {
        try {
            return getPhotoUri(Long.parseLong(contactId));
        } catch (Exception e) {
            return null;
        }
    }


    public static final Uri getPhotoUri(long contactId) {
        ContentResolver contentResolver = AppState.getApplication().getContentResolver();

        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    public static Uri getPhotoFromNumber(String phoneNumber) {
        try {
            final Uri returnUri = getPhotoUri(Long.valueOf(fetchContactIdFromPhoneNumber(phoneNumber)));
//            Log.e("Blend", String.valueOf(returnUri));
            return returnUri;
        } catch (Exception e) {
            return null;
        }

    }

    public static Drawable getDrawableFromNumber(String phoneNumber) {

        final Uri returnUri = getPhotoUri(Long.valueOf(fetchContactIdFromPhoneNumber(phoneNumber)));
        try {
            InputStream inputStream = AppState.getApplication().getContentResolver().openInputStream(returnUri);
            return Drawable.createFromStream(inputStream, returnUri.toString() );
        } catch (FileNotFoundException e) {
            return AppState.getApplication().getResources().getDrawable(R.drawable.ic_launcher);
        }

    }

    /**
     * *
     * @param phoneNum Phone Number for the contact whose name you seek
     * @return The Name if found, otherwise the phone number
     */
    public static final String getContactName(String phoneNum) {
        if (AppState.isNullOrEmpty(phoneNum))
            return "";

        Cursor cursor = null;

        try {
            Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNum));

            cursor = AppState.getApplication().getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);

            cursor.moveToFirst();
            final String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
//            Log.w(LOG_TAG, name);
            return name;
        } catch (Exception e) {
//            Log.e(LOG_TAG, phoneNum);
            if (AppState.DEBUG) {
                e.printStackTrace();
            }
            return phoneNum;
        } finally {
            if (cursor != null && !cursor.isClosed())
            cursor.close();
        }

    }

    public static final String[] getContactInfo() {
        // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

    return null;

    }

    public InputStream openDisplayPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd = AppState.getApplication().getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
            return fd.createInputStream();
        } catch (IOException e) {
//            return AppState.getApplication().getDrawable(R.drawable.ic_launcher);
            return null;
        }
    }


}

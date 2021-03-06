package com.untappedkegg.blend;

import android.app.Application;
import android.content.Context;
import android.drm.DrmManagerClient;
import android.os.StrictMode;
import android.provider.SearchRecentSuggestions;
import android.telephony.TelephonyManager;

import com.untappedkegg.blend.data.Contact;


/**
 * Created by kyle on 1/7/15.
 */
public class AppState extends Application {

    @Deprecated
    /**
     * use {@code BuildConfig.DEBUG instead}, it gets set automagically
     */
    public static final boolean DEBUG = true;

    private SearchRecentSuggestions mRecentSuggestions;
    private TelephonyManager mTelephonyManager;
    private DrmManagerClient mDrmManagerClient;
    /* ----- VARIABLES ----- */
    private static Application instance;

    /*----- BUNDLE KEYS -----*/
    public static final String KEY_MSG_ID = "message_id";
    public static final String KEY_MSG_NAME = "message_name";
    public static final String KEY_MSG_PHOTO = "message_photo";

    /* ----- CONSTRUCTORS ----- */
    public AppState() {
        instance = this;
    }

    /**
     * @return the global Application instance.
     */
    public static Application getApplication() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            // Log tag for enabling/disabling StrictMode violation log. This will dump a stack
            // in the log that shows the StrictMode violator.
            // To enable: adb shell setprop log.tag.Mms:strictmode DEBUG
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        }

        // Load the default preference values
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        MmsConfig.init(this);
        Contact.init(this);
//        Conversation.init(this);

    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    /**
     * @return Returns the TelephonyManager.
     */
    public TelephonyManager getTelephonyManager() {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephonyManager;
    }

    /**
     * Returns the content provider wrapper that allows access to recent searches.
     * @return Returns the content provider wrapper that allows access to recent searches.
     */
    public SearchRecentSuggestions getRecentSuggestions() {
        /*
        if (mRecentSuggestions == null) {
            mRecentSuggestions = new SearchRecentSuggestions(this,
                    SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
        }
        */
        return mRecentSuggestions;
    }

    public DrmManagerClient getDrmManagerClient() {
        if (mDrmManagerClient == null) {
            mDrmManagerClient = new DrmManagerClient(getApplicationContext());
        }
        return mDrmManagerClient;
    }

    /**
     * Tests {@code str} for a null or "".
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Case insensitive check if a String starts with a specified prefix.
     * <p/>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.
     * <p/>
     * <pre>
     * AppState.startsWithIgnoreCase(null, null)      = true
     * AppState.startsWithIgnoreCase(null, "abcdef")  = false
     * AppState.startsWithIgnoreCase("abc", null)     = false
     * AppState.startsWithIgnoreCase("abc", "abcdef") = true
     * AppState.startsWithIgnoreCase("abc", "ABCDEF") = true
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case insensitive, or
     * both <code>null</code>
     * @see java.lang.String#startsWith(String)
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        return prefix.length() <= str.length() && str.regionMatches(true, 0, prefix, 0, prefix.length());
    }



}

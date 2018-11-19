package com.makeuseof.muocore;

import android.content.SharedPreferences;

/**
 * Global Preferences
 */

public class GlobalPrefs {

    public final static int TEXT_SIZE_SMALL = 0;
    public final static int TEXT_SIZE_MEDIUM = 1;
    public final static int TEXT_SIZE_LARGE = 2;

    public final static String PUSH_NOTIFICATION_ENABLED = "push_notification_enabled";
    public final static String SUBSCRIBED_TO_DAILY_PUSHES = "subscribed_to_daily_pushes";
    public final static String APP_VERSION_CHECK_UPDATE = "app_version_check_update";
    public final static String APP_NEW_VERSION_AVAILABLE = "app_new_version_available";
    public final static String POST_TEXT_SIZE = "post_text_size";
    public final static String POST_LAST_ID = "post_last_id";
    public final static String CHATS_LAST_CHECK_TIMESTAMP = "chats_last_check_timestamp";
    public final static String CATS_LAST_CHECK_TIMESTAMP = "cats_last_check_timestamp";
    public final static String BOOKMARKS_LAST_CHECK_TIMESTAMP = "bookmarks_last_check_timestamp";
    public final static String SYNC_BOOKMARKS_PROMPT = "sync_bookmarks_prompt";
    public final static String BLACK_WHITE_THEME_ACTIVATED = "black_white_theme_activated";

    private SharedPreferences mPreferences;

    private boolean mPushNotificationEnabled;
    private boolean mSubscribedToDailyPushes;
    private long mAppVersionCheckUpdate;
    private boolean mAppNewVersionAvailable;
    private int mPostTextSize;
    private int mPostLastId;
    private long mCatsLastCheckTimestamp;
    private long mBookmarksLastCheckTimestamp;
    private boolean mSyncBookmarksPrompt;
    private boolean isBlackWhiteTheme;

    public GlobalPrefs(SharedPreferences preferences){
        mPreferences = preferences;
        load();
    }

    public GlobalPrefs setPushNotificationEnabled(boolean pushNotificationEnabled) {
        mPushNotificationEnabled = pushNotificationEnabled;
        return this;
    }

    public GlobalPrefs setSubscribedToDailyPushes(boolean subscribedToDailyPushes) {
        mSubscribedToDailyPushes = subscribedToDailyPushes;
        return this;
    }
    public boolean isSubscribedToDailyPushes() {
        return mSubscribedToDailyPushes;
    }

    public GlobalPrefs setBookmarksLastCheckTimestamp(long bookmarksLastCheckTimestamp) {
        mBookmarksLastCheckTimestamp = bookmarksLastCheckTimestamp;
        return this;
    }

    public long getBookmarksLastCheckTimestamp() {
        return mBookmarksLastCheckTimestamp;
    }

    public boolean isPushNotificationEnabled() {
        return mPushNotificationEnabled;
    }

    public long getAppVersionCheckUpdate() {
        return mAppVersionCheckUpdate;
    }

    public GlobalPrefs setSyncBookmarksPrompt(boolean syncBookmarksPrompt) {
        mSyncBookmarksPrompt = syncBookmarksPrompt;
        return this;
    }

    public boolean isSyncBookmarksPrompt() {
        return mSyncBookmarksPrompt;
    }

    public GlobalPrefs setAppVersionCheckUpdate(long appVersionCheckUpdate) {
        mAppVersionCheckUpdate = appVersionCheckUpdate;
        return this;
    }

    public boolean isAppNewVersionAvailable() {
        return mAppNewVersionAvailable;
    }

    public GlobalPrefs setAppNewVersionAvailable(boolean appNewVersionAvailable) {
        mAppNewVersionAvailable = appNewVersionAvailable;
        return this;
    }

    public GlobalPrefs setPostLastId(int postLastId) {
        mPostLastId = postLastId;
        return this;
    }

    public int getPostLastId() {
        return mPostLastId;
    }

    public boolean hasPostLastId(){
        return mPostLastId != 0;
    }

    public GlobalPrefs setPostTextSize(int textSize) {
        mPostTextSize = textSize;
        return this;
    }

    public int getPostTextSize(){
        return mPostTextSize;
    }

    public long getCatsLastCheckTimestamp() {
        return mCatsLastCheckTimestamp;
    }

    public GlobalPrefs setCatsLastCheckTimestamp(long catsLastCheckTimestamp) {
        mCatsLastCheckTimestamp = catsLastCheckTimestamp;
        return this;
    }

    public void load(){
        mPushNotificationEnabled = mPreferences.getBoolean(PUSH_NOTIFICATION_ENABLED, true);
        mSubscribedToDailyPushes = mPreferences.getBoolean(SUBSCRIBED_TO_DAILY_PUSHES, false);
        mAppVersionCheckUpdate = mPreferences.getLong(APP_VERSION_CHECK_UPDATE, 0);
        mPostTextSize = mPreferences.getInt(POST_TEXT_SIZE, TEXT_SIZE_MEDIUM);
        mPostLastId = mPreferences.getInt(POST_LAST_ID, 0);
        mCatsLastCheckTimestamp = mPreferences.getLong(CATS_LAST_CHECK_TIMESTAMP, 0);
        mBookmarksLastCheckTimestamp = mPreferences.getLong(BOOKMARKS_LAST_CHECK_TIMESTAMP, 1446113672);
        mAppNewVersionAvailable = mPreferences.getBoolean(APP_NEW_VERSION_AVAILABLE, false);
        mSyncBookmarksPrompt = mPreferences.getBoolean(SYNC_BOOKMARKS_PROMPT, false);
        isBlackWhiteTheme = mPreferences.getBoolean(BLACK_WHITE_THEME_ACTIVATED, false);
    }

    public boolean commit(){
        return mPreferences.edit()
                .putBoolean(PUSH_NOTIFICATION_ENABLED, mPushNotificationEnabled)
                .putBoolean(SUBSCRIBED_TO_DAILY_PUSHES, mSubscribedToDailyPushes)
                .putLong(APP_VERSION_CHECK_UPDATE, mAppVersionCheckUpdate)
                .putInt(POST_TEXT_SIZE, mPostTextSize)
                .putInt(POST_LAST_ID, mPostLastId)
                .putLong(CATS_LAST_CHECK_TIMESTAMP, mCatsLastCheckTimestamp)
                .putLong(BOOKMARKS_LAST_CHECK_TIMESTAMP, mBookmarksLastCheckTimestamp)
                .putBoolean(APP_NEW_VERSION_AVAILABLE, mAppNewVersionAvailable)
                .putBoolean(SYNC_BOOKMARKS_PROMPT, mSyncBookmarksPrompt)
                .putBoolean(BLACK_WHITE_THEME_ACTIVATED, isBlackWhiteTheme)
                .commit();
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public boolean isBlackWhiteTheme() {
        return isBlackWhiteTheme;
    }

    public GlobalPrefs setBlackWhiteTheme(boolean isBlackWhiteTheme) {
        this.isBlackWhiteTheme = isBlackWhiteTheme;
        return this;
    }
}

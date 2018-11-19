package com.makeuseof.muocore;

/**
 * Created by Alisher Alikulov on 12/26/16.
 * for project MuoLibTest
 * with Android Studio
 */

public class CoreSharedConstants {
    public static final String KEY_POST_ID = "post_id";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_POST_IMG_URL = "image_url";
    public static final String SAVED_POST_UPDATED_MESSAGE = "Saved_posts_updated";

    public class Broadcast{
        public static final String SYSTEM_TIME_ERROR = "check_system_time";
        public static final String NEW_POSTS_MESSAGE = "new_posts_message";
        public static final String SUBSCRIPTIONS_UPDATED_MESSAGE = "subscriptions_updated_message";
        public static final String UNREAD_COUNT_UPDATED_MESSAGE = "unread_count_updated";
        public static final String POST_LIKED = "post_liked";
        public static final String POST_OPENED_FIRST_TIME = "post_opened";
        public static final String MAIN_MENU_UPDATE = "main_menu_update";
        public static final String DIALOG_BROWSER_DISMISS_ACTION = "dialog_browser_dismissed";
    }
}

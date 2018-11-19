package com.makeuseof.muocore.context;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.makeuseof.muocore.GlobalPrefs;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.db.MuoCoreDBHelper;
import com.makeuseof.muocore.exceptions.MuoCoreContextNotInitializedException;
import com.makeuseof.muocore.models.IPostsProvider;
import com.makeuseof.muocore.models.MuoCorePostsProvider;
import com.makeuseof.muocore.models.UserManager;
import com.makeuseof.muocore.ui.delegates.AuthorPageDelegate;
import com.makeuseof.muocore.ui.delegates.BookmarkDelegate;
import com.makeuseof.muocore.ui.delegates.GrouviChatsDelegate;
import com.makeuseof.muocore.ui.delegates.PostShareDelegate;
import com.makeuseof.muocore.ui.delegates.PostUrlSchemaDelegate;
import com.makeuseof.muocore.ui.delegates.StatsDelegate;
import com.makeuseof.muocore.ui.listeners.CoreBaseActivityListener;

/**
 * Created by Alisher Alikulov on 12/26/16.
 * for project MuoLibTest
 * with Android Studio
 */

public class MuoCoreContext {
    private static MuoCoreContext instance;

    private String apiUrl;
    private String siteUrl;
    private String apiAccessId;
    private String apiSecretKey;
    private UserManager userManager;
    private GlobalPrefs globalPrefs;
    private Context context;
    private BasicAuthCredentials basicAuthCredentials;
    private CoreBaseActivityListener coreBaseActivityListener = new CoreBaseActivityListener();
    private boolean needXAccessKey = false;
    private boolean blackWhiteThemeEnabled = false;
    private boolean muoApp = false;
    private int defaultPlaceholder = R.drawable.default_placeholder;
    private PostShareDelegate postShareDelegate = new PostShareDelegate();
    private BookmarkDelegate bookmarkDelegate = new BookmarkDelegate();
    private IPostsProvider postProvider;
    private AuthorPageDelegate authorsDelegate = new AuthorPageDelegate();
    private StatsDelegate statsDelegate = new StatsDelegate();
    private GrouviChatsDelegate grouviChatsDelegate = new GrouviChatsDelegate();
    private IPostsProvider secondaryPostProvider;
    private PostUrlSchemaDelegate postUrlSchemaDelegate = new PostUrlSchemaDelegate();
    private boolean autoRefreshOnHomePageEnabled = false;

    public static void init(String apiUrl, Context context){
        init(apiUrl, context, new UserManager());
    }

    public static void init(String apiUrl, Context context, UserManager userManager){
        instance = new MuoCoreContext();
        instance.apiUrl = apiUrl;
        instance.siteUrl = apiUrl;
        instance.userManager = userManager;
        instance.context = context;
        instance.globalPrefs = new GlobalPrefs(context.getSharedPreferences("MUO Core SF", 0));
        instance.bookmarkDelegate.setDbHelper(new MuoCoreDBHelper(context));
    }

    public static MuoCoreContext getInstance() {
        if(instance == null){
            throw new MuoCoreContextNotInitializedException();
        }
        return instance;
    }

    private MuoCoreContext(){}

    public GlobalPrefs getGlobalPrefs() {
        return globalPrefs;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiAccessId() {
        return apiAccessId;
    }

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Context getContext() {
        return context;
    }

    public CoreBaseActivityListener getCoreBaseActivityListener() {
        return coreBaseActivityListener;
    }

    public void setCoreBaseActivityListener(CoreBaseActivityListener coreBaseActivityListener) {
        this.coreBaseActivityListener = coreBaseActivityListener;
    }

    public boolean needBasicAuth() {
        return basicAuthCredentials != null;
    }

    public void setBasicAuthCredentials(String username, String password) {
        this.basicAuthCredentials = BasicAuthCredentials.make(username, password);
    }

    public BasicAuthCredentials getBasicAuthCredentials() {
        return basicAuthCredentials;
    }

    public boolean needRandomXAccessKey() {
        return needXAccessKey;
    }

    public void setNeedXAccessKey(boolean needXAccessKey) {
        this.needXAccessKey = needXAccessKey;
    }

    public PostShareDelegate getPostShareDelegate() {
        return postShareDelegate;
    }

    public void setPostShareDelegate(PostShareDelegate postShareDelegate) {
        this.postShareDelegate = postShareDelegate;
    }

    public BookmarkDelegate getBookmarkDelegate() {
        return bookmarkDelegate;
    }

    public void setBookmarkDelegate(BookmarkDelegate bookmarkDelegate) {
        this.bookmarkDelegate = bookmarkDelegate;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setPostProvider(IPostsProvider postProvider) {
        this.postProvider = postProvider;
    }

    public IPostsProvider getPostProvider() {
        if(postProvider == null){
            postProvider = MuoCorePostsProvider.newInstance();
        }
        return postProvider;
    }

    public AuthorPageDelegate getAuthorsDelegate() {
        return authorsDelegate;
    }

    public void setAuthorsDelegate(AuthorPageDelegate authorsDelegate) {
        this.authorsDelegate = authorsDelegate;
    }

    public StatsDelegate getStatsDelegate() {
        return statsDelegate;
    }

    public void setStatsDelegate(StatsDelegate statsDelegate) {
        this.statsDelegate = statsDelegate;
    }

    public boolean isBlackWhiteThemeEnabled() {
        return blackWhiteThemeEnabled;
    }

    public void setBlackWhiteThemeEnabled(boolean blackWhiteThemeEnabled) {
        this.blackWhiteThemeEnabled = blackWhiteThemeEnabled;
    }

    public GrouviChatsDelegate getGrouviChatsDelegate() {
        return grouviChatsDelegate;
    }

    public void setGrouviChatsDelegate(GrouviChatsDelegate grouviChatsDelegate) {
        this.grouviChatsDelegate = grouviChatsDelegate;
    }

    public void setSecondaryPostProvider(IPostsProvider secondaryPostProvider) {
        this.secondaryPostProvider = secondaryPostProvider;
    }

    public IPostsProvider getSecondaryPostProvider() {
        if(secondaryPostProvider == null){
            secondaryPostProvider = MuoCorePostsProvider.newInstance();
        }
        return secondaryPostProvider;
    }

    public int getDefaultPlaceholder() {
        return defaultPlaceholder;
    }

    public void setDefaultPlaceholder(@DrawableRes int defaultPlaceholder) {
        this.defaultPlaceholder = defaultPlaceholder;
    }

    public boolean isMuoApp() {
        return muoApp;
    }

    public void setMuoApp(boolean muoApp) {
        this.muoApp = muoApp;
    }

    public PostUrlSchemaDelegate getPostUrlSchemaDelegate() {
        return postUrlSchemaDelegate;
    }

    public void setPostUrlSchemaDelegate(PostUrlSchemaDelegate postUrlSchemaDelegate) {
        this.postUrlSchemaDelegate = postUrlSchemaDelegate;
    }

    public boolean isAutoRefreshOnHomePageEnabled() {
        return autoRefreshOnHomePageEnabled;
    }

    public void setAutoRefreshOnHomePageEnabled(boolean autoRefreshOnHomePageEnabled) {
        this.autoRefreshOnHomePageEnabled = autoRefreshOnHomePageEnabled;
    }

    public void setApiAccessId(String apiAccessId) {
        this.apiAccessId = apiAccessId;
    }

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
    }
}

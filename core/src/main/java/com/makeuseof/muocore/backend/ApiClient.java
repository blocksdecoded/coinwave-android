package com.makeuseof.muocore.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;
import com.makeuseof.muocore.BuildConfig;
import com.makeuseof.muocore.context.BasicAuthCredentials;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.UserManager;
import com.makeuseof.muocore.util.SimpleSHA1;

import java.util.Date;
import java.util.Random;

public class ApiClient {

    public enum RequestType {
        GET,
        POST,
        PUT,
        DELETE
    }

    private final String mApiBaseUrl;
    private final AsyncHttpClient mClient;
    private final SyncHttpClient mSyncClient;
    private final PersistentCookieStore mCookieStore;
    private final Context mContext;
    private final UserManager userManager;

    public ApiClient(Context context) {
        mApiBaseUrl = MuoCoreContext.getInstance().getApiUrl();
        userManager = MuoCoreContext.getInstance().getUserManager();

        mCookieStore = new PersistentCookieStore(context);
        mContext = context;

        mClient = new AsyncHttpClient();
        mClient.setCookieStore(mCookieStore);

        mSyncClient = new SyncHttpClient();
    }

    public AsyncHttpClient getClient() {
        return mClient;
    }

    public SyncHttpClient getSyncClient() {
        return mSyncClient;
    }

    public void clearCookieStore() {
        mCookieStore.clear();
    }

    public void cancelAllRequests() {
        mClient.cancelRequests(mContext, true);
    }

    public void perform(ApiRequest request) {
        mClient.removeAllHeaders();

        String accessId = MuoCoreContext.getInstance().getApiAccessId();
        String secretKey = MuoCoreContext.getInstance().getApiSecretKey();
        long currentTimestamp = new Date().getTime() / 1000;
        if(accessId != null && secretKey != null) {
            mClient.addHeader("X-Api-Auth", accessId + "_" + currentTimestamp + "_" + SimpleSHA1.SHA1(currentTimestamp + secretKey));
        }
        mClient.addHeader("X-Client-Os", "android");
        mClient.addHeader("User-Agent", "android_library_"+ BuildConfig.VERSION_NAME);

        if(MuoCoreContext.getInstance().needBasicAuth()) {
            BasicAuthCredentials credentials = MuoCoreContext.getInstance().getBasicAuthCredentials();
            mClient.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        }

        if(MuoCoreContext.getInstance().needRandomXAccessKey()) {
            mClient.addHeader("X-Access-Key", randomString(32));
        }

        if(userManager.hasUser() /** buggy && request.isNeedToken()*/) {
            mClient.addHeader("Authorization", String.format("Token token=%s", userManager.getUser().token));
        }

        Log.i("MUO REQUEST", request.url + " --- " + request.params.toString());

        switch (request.type) {
            case GET:
                mClient.get(mContext, getAbsoluteUrl(request.url), request.params, request);
                break;
            case POST:
                mClient.post(mContext, getAbsoluteUrl(request.url), request.params, request);
                break;
            case PUT:
                mClient.put(mContext, getAbsoluteUrl(request.url), request.params, request);
                break;
            case DELETE:
                mClient.delete(mContext, getAbsoluteUrl(request.url), request);
                break;
        }
    }

    private String randomString(int length) {
        Random random = new Random();
        StringBuilder string = new StringBuilder();
        String alphabet = "qwertyuiopasdfghjklzxcvbnm1234567890";
        for(int i=0;i<length;i++){
            string.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return string.toString();
    }

    private String getAbsoluteUrl(String relativeUrl) {
        if(relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://")){
            return relativeUrl;
        }
        return mApiBaseUrl + relativeUrl;
    }


    public boolean hasInternet(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return hasInternet;
    }

}

/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.backend;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.R;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Base Api Request
 */

public abstract class ApiRequest extends TextHttpResponseHandler {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static final ApiClient apiClient = new ApiClient(MuoCoreContext.getInstance().getContext());

    private final Context mContext;
    public final String url;
    public final ApiClient.RequestType type;
    public final RequestParams params;

    protected ApiRequest(ApiClient.RequestType type, String url, RequestParams params, Context context) {
        this.type = type;
        this.url = url;
        this.params = params == null ? new RequestParams() : params;
        this.mContext = context;
    }

    public void onPostProcess(){}

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
//        Log.i("RESPONSE", "status=" + statusCode + ", body=" + responseBody);
        try {
            onPostProcess();
        } catch(Exception e){
            e.printStackTrace();
        }

        if(isNeedParseJSON()) {
            try {
                onSuccess(objectMapper.readValue(responseBody, JsonNode.class));
            } catch (Exception e) {
//                Log.d("ololo", "Request fail " + e.getMessage());
                e.printStackTrace();
                onFailure(e);
            }
        }  else {
            onSuccess(responseBody);
        }
    }

    public void onFailure(Exception e){
        if(e instanceof JsonParseException){
            onNoConnection();
        } else {
            onFailure(e.getMessage());
        }
    }

    protected abstract void onSuccess(JsonNode resultNode);

    public void onSuccess(String responseBody) {

    }



    @Override
    public void onFailure(int statusCode, Header[] headers, String s, Throwable throwable) {
//        Log.i("RESPONSE", "status=" + statusCode + ", body=" + s + ", url=" + url);
        try {
            onPostProcess();
        } catch(Exception e){
            e.printStackTrace();
        }

        if(statusCode == 401){
            try {
                JsonNode node = objectMapper.readValue(s, JsonNode.class);
                if(node.get("error").get("code").intValue() == 42) {
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.SYSTEM_TIME_ERROR));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            onNoConnection();
        } else if (throwable instanceof IOException && s == null) {
            onNoConnection();
        } else if(statusCode == 500) {
            onNoConnection();
        } else {
            Log.e("MUO Error", "error", throwable);
            onFailure(s);
        }
    }

    public void onNoConnection() {
        onFailure(mContext.getString(R.string.no_internet_connection_or_server_unreachable));
    }

    public String getUrl() {
        return url;
    }

    public void onFailure(String message) {
        if(message == null || message.isEmpty()) {
            return;
        }

        try {
            Log.e("MUO_ERROR", "There was an error which should not happen!");
            throw new Exception("There was an error which should not happen!");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void retry() {
        ApiRequest.apiClient.perform(this);
    }

    public boolean isNeedParseJSON(){
        return true;
    }

    public boolean isNeedToken(){
        return false;
    }

}

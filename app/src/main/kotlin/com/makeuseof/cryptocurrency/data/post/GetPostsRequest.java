package com.makeuseof.cryptocurrency.data.post;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.cryptocurrency.BuildConfig;
import com.makeuseof.muocore.backend.ApiClient;
import com.makeuseof.muocore.backend.ApiRequest;
import com.makeuseof.muocore.models.PublisherPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

public abstract class GetPostsRequest extends ApiRequest {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public GetPostsRequest(Context context, Date lastDate){
        super(ApiClient.RequestType.GET, BuildConfig.API_POST_URL + "/posts?cats=19", null, context);

        if (lastDate != null){
            params.put("last_item_datetime", dateFormat.format(lastDate));
        }

    }

    public abstract void onSuccess(List<PublisherPost> posts);

    @Override
    protected void onSuccess(JsonNode resultNode) {
        JsonNode rawPosts = resultNode.get("posts");
        try {
            List<PublisherPost> posts = new ArrayList<>(Arrays.asList(ApiRequest.objectMapper.treeToValue(rawPosts, PublisherPost[].class)));
            onSuccess(posts);
        } catch (JsonProcessingException e){
            Log.d("ololo", "Posts exception " + e.getMessage());
            onFailure(e.getMessage());
        }
    }

}

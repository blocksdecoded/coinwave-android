/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.backend;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.models.PublisherPost;

/**
 * Get Post Request
 */

public abstract class GetPostRequest extends ApiRequest {
    public GetPostRequest(Context context, String id) {
//        super(ApiClient.RequestType.GET, "https://pa.grouvi.im/makeuseof/posts/" + id, null, context);
        super(ApiClient.RequestType.GET, "http://muoarticles.muoapps.com/makeuseof/posts/" + id, null, context);
    }

    public abstract void onSuccess(IPost post);
    public abstract void onSuccess(PublisherPost post);

    @Override
    public void onSuccess(JsonNode resultNode) {
        try {
            PublisherPost post = ApiRequest.objectMapper.treeToValue(resultNode, PublisherPost.class);
            onSuccess(post);
        } catch (JsonProcessingException e){
//            Log.d("ololo", e.getMessage());
            onFailure(e.getMessage());
        }
//        try {
//            onSuccess(mapPost(resultNode));
//        } catch (JsonProcessingException e) {
//            onFailure(e.getMessage());
//        }
    }

    public abstract IPost mapPost(JsonNode resultNode) throws JsonProcessingException;
}

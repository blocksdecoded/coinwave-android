/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.backend;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.muocore.backend.interfaces.IPost;

/**
 * Get Post By URL Request
 */

public abstract class GetPostByUrlRequest extends ApiRequest {
    public GetPostByUrlRequest(Context context, String url) {
        super(ApiClient.RequestType.GET, "/posts/" , null, context);
        params.add("url", url);
    }

    public abstract void onSuccess(IPost post);

    @Override
    public void onSuccess(JsonNode resultNode) {
        try {
            onSuccess(mapPost(resultNode));
        } catch (JsonProcessingException e) {
            onFailure(e.getMessage());
        }
    }

    public abstract IPost mapPost(JsonNode resultNode) throws JsonProcessingException;
}

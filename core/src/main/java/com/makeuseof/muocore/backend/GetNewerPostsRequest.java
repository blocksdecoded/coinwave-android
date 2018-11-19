/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.muocore.backend.interfaces.IPost;

import java.util.List;

/**
 * Get Newer Posts Request
 */

public abstract class GetNewerPostsRequest extends ApiRequest {

    public GetNewerPostsRequest(Context context, Integer firstPostId, List<Integer> categoryIds) {
        super(ApiClient.RequestType.GET, "/posts", null, context);

        if(firstPostId != null && firstPostId > 0) {
            params.put("first_item_id", firstPostId);
        }
        params.put("with_body", true);
        params.put("without_css", true);

        for (Integer categoryId : categoryIds) {
            params.add("category_id[]", categoryId.toString());
        }
    }

    public abstract void onSuccess(List<IPost> posts);

    @Override
    public void onSuccess(JsonNode resultNode) {
        try {
            List<IPost> posts = mapPosts(resultNode);
            onSuccess(posts);
        } catch (JsonProcessingException e) {
            onFailure(e.getMessage());
        }
    }

    @NonNull
    public abstract List<IPost> mapPosts(JsonNode resultNode) throws JsonProcessingException;
}

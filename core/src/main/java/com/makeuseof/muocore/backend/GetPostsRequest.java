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
 * Get Posts Request
 */

public abstract class GetPostsRequest extends ApiRequest {

    public boolean isFirstLoad;
    public int count = 20;

    public GetPostsRequest(Context context, Integer lastPostId, List<Integer> categoryIds, boolean isFirstLoad, boolean withGroupIds, String searchQuery) {
        super(ApiClient.RequestType.GET, "/posts", null, context);

        if(searchQuery == null) {
            params.put("with_body", true);
        }

        params.put("without_css", true);
        params.put("per_page", count);

        if(searchQuery != null && !searchQuery.isEmpty()) {
            params.put("q", searchQuery);
        }

        if(withGroupIds) {
            params.put("with_group_ids", true);
        }

        if (lastPostId != null && lastPostId > 0) {
            params.put("last_item_id", lastPostId);
        }

        if(categoryIds != null) {
            for (Integer categoryId : categoryIds) {
                params.add("category_id[]", categoryId.toString());
            }
        }

        this.isFirstLoad = isFirstLoad;
    }

    public void setAuthorId(Integer id){
        params.add("author_id", String.valueOf(id));
    }

    public abstract void onSuccess(List<IPost> posts);

    @Override
    public void onSuccess(JsonNode resultNode) {
        try {
            List<IPost> posts = mapPosts(resultNode);
            posts = posts.subList(0, posts.size()>count?count:posts.size());

            onSuccess(posts);
        } catch (JsonProcessingException e) {
            onFailure(e.getMessage());
        }
    }

    @NonNull
    public abstract List<IPost> mapPosts(JsonNode resultNode) throws JsonProcessingException;
}

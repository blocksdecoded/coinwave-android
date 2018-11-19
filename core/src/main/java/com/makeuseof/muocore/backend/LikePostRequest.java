/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.backend;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Like Post Request
 */

public abstract class LikePostRequest extends ApiRequest {
    public LikePostRequest(Context context, int id) {
        super(ApiClient.RequestType.POST, "/posts/" + id + "/like", null, context);
    }

    public abstract void onSuccess(Boolean status);

    @Override
    public void onSuccess(JsonNode resultNode) {
        try {
            StatusResponse statusResponse = objectMapper.treeToValue(resultNode, StatusResponse.class);
            onSuccess(statusResponse.isOk());
        } catch (JsonProcessingException e) {
            onFailure(e.getMessage());
        }
    }

    public static class StatusResponse{
        @JsonProperty("status")
        public String status;

        public StatusResponse(){}

        public boolean isOk(){
            return status != null && status.equals("ok");
        }
    }
}

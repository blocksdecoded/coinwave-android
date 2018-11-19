/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.backend;

import android.content.Context;
import android.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Get StyleSheets Request
 */

public abstract class GetStylesheetsRequest extends ApiRequest {

    public GetStylesheetsRequest(Context context) {
        super(ApiClient.RequestType.GET, "/styles", null, context);
    }

    public abstract void onSuccess(String html);

    @Override
    public void onSuccess(JsonNode resultNode) {
        try {
            byte[] content = Base64.decode(resultNode.get("styles").asText(), Base64.DEFAULT);
            onSuccess(new String(content, "UTF-8"));
        } catch(Exception e) {
            onFailure(e.getMessage());
        }
    }
}

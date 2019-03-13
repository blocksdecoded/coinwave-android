package com.blocksdecoded.coinwave.data.bootstrap

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BootstrapResponse(
    @Expose @SerializedName("servers") val servers: ArrayList<String>
)
package com.blocksdecoded.coinwave

import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import okhttp3.OkHttpClient
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class CoinGlideModule : AppGlideModule() {

    private fun getMb(mb: Int): Long = 1024L * 1024 * mb

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSize = getMb(30) // 30mb
        val diskCacheSize = getMb(60) // 60mb

        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSize))
        builder.setMemoryCache(LruResourceCache(memoryCacheSize))

        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    override fun isManifestParsingEnabled(): Boolean = false
}
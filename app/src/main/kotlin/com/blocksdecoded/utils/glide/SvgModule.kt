package com.blocksdecoded.utils.glide

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule
import com.caverock.androidsvg.SVG
import java.io.InputStream

@GlideModule
class SvgGlideModule : LibraryGlideModule() {
    override fun registerComponents(
        context: Context,
        glide: Glide,
        registry: Registry
    ) {
        registry.register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
                .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }
}
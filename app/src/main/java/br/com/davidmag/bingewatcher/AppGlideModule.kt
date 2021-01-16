package br.com.davidmag.bingewatcher


import android.content.Context
import br.com.davidmag.bingewatcher.presentation.di.presentationComponent
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import java.io.InputStream
import javax.inject.Inject

@GlideModule
class AppGlideModule : AppGlideModule(){

    @Inject
    lateinit var factory : OkHttpUrlLoader.Factory

    init {
        presentationComponent.inject(this)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
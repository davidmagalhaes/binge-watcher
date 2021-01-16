package br.com.davidmag.bingewatcher.presentation.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.davidmag.bingewatcher.infra.App

import com.bumptech.glide.module.AppGlideModule

private val presentationComponent : PresentationComponent by lazy {
    DaggerPresentationComponent
        .builder()
        .applicationComponent(App.applicationComponent)
        .build()
}

val AppCompatActivity.presentationComponent by lazy {
    presentationComponent
}

val Fragment.presentationComponent by lazy {
    presentationComponent
}

val AppGlideModule.presentationComponent by lazy {
    presentationComponent
}


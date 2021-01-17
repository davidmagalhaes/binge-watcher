package br.com.davidmag.bingewatcher.presentation.di

import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.ShowViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun provideHomeViewModel() = HomeViewModel()

    @Provides
    fun provideShowViewModel() = ShowViewModel()
}
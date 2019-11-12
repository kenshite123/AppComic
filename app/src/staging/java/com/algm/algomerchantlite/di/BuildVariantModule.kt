package com.algm.algomerchantlite.di

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by TuanNguyen on 12/12/17.
 */
@Module
 class BuildVariantModule {
    @Provides @Singleton @Named("uri") fun baseUri():String="http://49b86be0.ap.ngrok.io"
}
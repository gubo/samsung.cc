
package com.gubo.samsung.cc

import javax.inject.*

import dagger.*

import com.gubo.samsung.cc.home.*
import com.gubo.samsung.cc.open.*

/**
 * Created by GUBO on 7/20/2017.
 */
@Module
class AppModule( val application:CCApplication )
{
    @Provides
    @Singleton
    fun provideDataBus() : DataBus = DataBus()

    @Provides
    @Singleton
    fun provideEventBus() : EventBus = EventBus()

    @Provides
    @Singleton
    fun provideDomain() : Domain = Domain()

    @Provides
    @Singleton
    fun provideHomePresenter() : HomePresenter = HomePresenter()

    @Provides
    @Singleton
    fun provideOpenPresenter() : OpenPresenter = OpenPresenter()
}


package com.gubo.samsung.cc

import javax.inject.*

import dagger.*

import com.gubo.samsung.cc.home.*
import com.gubo.samsung.cc.open.*
import com.gubo.samsung.cc.preview.*

/**
 * Created by GUBO on 7/20/2017.
 */
@Singleton
@Component( modules = arrayOf( AppModule::class ) )
interface AppComponent
{
    fun inject( homeActivity: HomeActivity )
    fun inject( homePresenter: HomePresenter )
    fun inject( openActivity: OpenActivity )
    fun inject( openPresenter: OpenPresenter )
    fun inject( previewFragment: PreviewFragment )
    fun inject( previewPresenter: PreviewPresenter )
}
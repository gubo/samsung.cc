
package com.gubo.samsung.cc

import android.app.*

/**
 * Created by GUBO on 7/20/2017.
 */
class CCApplication : Application()
{
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule( AppModule( this ) )
                .build()
    }
}
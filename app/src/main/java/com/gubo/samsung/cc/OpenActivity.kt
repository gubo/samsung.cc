
package com.gubo.samsung.cc

import javax.inject.*

import android.os.*
import android.support.v7.app.*
import android.support.v7.widget.*

import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import com.google.gson.*

import gubo.slipwire.*

import com.gubo.samsung.cc.model.*
import com.gubo.samsung.cc.open.*

/**
 * Created by GUBO on 7/21/2017.
 */
class OpenActivity : AppCompatActivity()
{
    private val gson = GsonBuilder().setPrettyPrinting().create()

    @Inject lateinit var openPresenter: OpenPresenter
    @Inject lateinit var eventbus : EventBus

    private lateinit var album: Album

    private var disposable: Disposable? = null

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        CCApplication.appComponent.inject( this )

        try {
            if ( savedInstanceState == null ) {
                val json = intent.getStringExtra( "album" )
                album = gson.fromJson( json,Album::class.java )
            }
        } catch ( x:Exception ) {
            exception( x )
        }

        setContentView( R.layout.open )

        val toolbar = findViewById( R.id.opentoolbar ) as Toolbar
        setSupportActionBar( toolbar )
        getSupportActionBar()?.setDisplayHomeAsUpEnabled( true )
        getSupportActionBar()?.setDisplayShowHomeEnabled( true )
        getSupportActionBar()?.setTitle( "" )
    }

    override fun onStart() {
        super.onStart()

        disposable = eventbus.toObservable()
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { event -> onEvent( event ) },
                        { error -> exception( error ) }
                )

        openPresenter.using( album ).bind( OpenAdapter( window.decorView ).using( openPresenter ) )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStop() {
        super.onStop()

        disposable?.dispose()
        openPresenter.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        if ( !isChangingConfigurations ) {
            openPresenter.release()
            if ( eventbus.hasObservers() ) { warning { "eventbus has observers" } }
        }
    }

    private fun onEvent( any:Any? ) {}
}
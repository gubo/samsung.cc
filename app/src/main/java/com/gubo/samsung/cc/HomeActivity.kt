
package com.gubo.samsung.cc

import javax.inject.*

import android.os.*
import android.view.*
import android.content.*
import android.support.v7.app.*

import com.google.gson.*

import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import gubo.slipwire.*

import com.gubo.samsung.cc.home.*
import com.gubo.samsung.cc.event.*
import com.gubo.samsung.cc.model.*

/**
 * Created by GUBO on 7/20/2017.
 */
class HomeActivity : AppCompatActivity()
{
    @Inject lateinit var homePresenter: HomePresenter
    @Inject lateinit var eventbus : EventBus

    private var disposable: Disposable? = null

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        CCApplication.appComponent.inject( this )

        setContentView( R.layout.home )
    }

    override fun onStart() {
        super.onStart()

        disposable = eventbus.toObservable()
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { event -> onEvent( event ) },
                        { error -> exception( error ) }
                )

        homePresenter.bind( HomeAdapter( window.decorView ).using( homePresenter ) )
    }

    override fun onStop() {
        super.onStop()

        disposable?.dispose()
        homePresenter.unbind()
    }

    fun onLeavePreview( view : View) {
        if ( getSupportFragmentManager().backStackEntryCount >= 1 ) {
            getSupportFragmentManager().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if ( !isChangingConfigurations ) {
            homePresenter.release()
            if ( eventbus.hasObservers() ) { warning { "eventbus has observers" } }
        }
    }

    private fun onEvent( any:Any? ) {
        if ( any is OpenAlbumEvent ) {
            open( any.album )
        }
        if ( any is PreviewAlbumEvent ) {
            preview( any.album )
        }
    }

    private fun open( album:Album ) {
        val intent = Intent( baseContext,OpenActivity::class.java )
        val json = GsonBuilder().setPrettyPrinting().create().toJson( album )
        intent.putExtra( "album",json )
        startActivityForResult( intent,100 )
    }

    private fun preview( album:Album ) {
        val previewFragment = createPreviewFragment( album )
        getSupportFragmentManager()
                .beginTransaction()
                .add( R.id.homepreviewholder,previewFragment )
                .addToBackStack( "preview" )
                .commit();
    }
}
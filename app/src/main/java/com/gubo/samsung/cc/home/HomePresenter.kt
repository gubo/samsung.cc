
package com.gubo.samsung.cc.home

import javax.inject.*

import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import gubo.slipwire.*

import com.gubo.samsung.cc.*
import com.gubo.samsung.cc.model.*
import com.gubo.samsung.cc.event.*

/**
 * Created by GUBO on 7/21/2017.
 */
class HomePresenter : Presenter<HomeDisplay>,DataSource<Album>,HomeListener
{
    private val compositeDisposable = CompositeDisposable()
    private val albums = ArrayList<Album>() // TODO: affect infinite list scrolling by backing the data with directional decaying list

    private var display : HomeDisplay? = null
    private var firstVisiblePosition : Int = 0
    private var disposable:Disposable? = null

    @Inject lateinit var eventbus : EventBus
    @Inject lateinit var databus : DataBus
    @Inject lateinit var domain: Domain

    init {
        CCApplication.appComponent.inject( this )
    }

    override fun bind( d:HomeDisplay ) {
        debug( { "HomePresenter.bind ${d.javaClass.simpleName}" } )

        display = d
        display?.setHomeListener( this )
        display?.setItemCount( albums.size )
        display?.setPosition( firstVisiblePosition )

        if ( disposable == null ) {
            disposable = databus.toObservable()
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe(
                            { data -> onData( data ) },
                            { error -> exception( error ) }
                    )
        }

        if ( albums.isEmpty() ) {
            fetchAlbums( 0,100 )
        }
    }

    override fun unbind() {
        firstVisiblePosition = display?.getFirstVisiblePosition() ?: 0
        display?.release()
        display = null

        debug( { "HomePresenter.unbind" } )
    }

    override fun release() {
        firstVisiblePosition = 0
        display?.release()
        display = null

        disposable?.dispose()
        disposable = null

        compositeDisposable.clear()
        albums.clear()

        debug( { "HomePresenter.release" } )
    }

    override fun getDataFor( position:Int ) : Album {
        try {
            return albums[ position ]
        } catch ( x:Exception ) {
            exception( x )
        }
        return Album()
    }

    override fun getReadyFor( start:Int,count:Int ) {}
    override fun requestRefresh() {}

    override fun onSelectAlbum( album:Album ) {
        eventbus.send( OpenAlbumEvent( album ) )
    }

    override fun onPreviewAlbum(album: Album) {
        eventbus.send( PreviewAlbumEvent( album ) )
    }

    private fun fetchAlbums( start:Int,count:Int ) {
        compositeDisposable.clear()

        val observable = domain?.fetchAlbums( start,count )
        val disposable = observable
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                { next -> run{ onNext( next ) } },
                { error -> run{ onError( error ) } },
                { run{ onComplete() } }
        )

        compositeDisposable.add( disposable )
    }

    private fun onNext( album:Album ) {
        val indexOf = albums.indexOf( album )
        when ( indexOf ) {
            -1 -> { albums.add( album ) }
            else -> { albums[ indexOf ] = album }
        }
    }

    private fun onComplete() {
        display?.setItemCount( albums.size )
    }

    private fun onError( x:Throwable ) {
        exception( x )
    }

    private fun onData( any:Any? ) {
        // TODO: update albums list from any
    }
}
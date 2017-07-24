
package com.gubo.samsung.cc.open

import javax.inject.*

import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import gubo.slipwire.*

import com.gubo.samsung.cc.*
import com.gubo.samsung.cc.model.*

/**
 * Created by GUBO on 7/24/2017.
 */
class OpenPresenter : Presenter<OpenDisplay>, DataSource<Photo>,OpenListener
{
    private val compositeDisposable = CompositeDisposable()
    private val photos = ArrayList<Photo>() // TODO: affect infinite list scrolling by backing the data with directional decaying list

    private var display : OpenDisplay? = null
    private var firstVisiblePosition : Int = 0
    private var disposable:Disposable? = null
    private var album:Album? = null

    @Inject lateinit var eventbus : EventBus
    @Inject lateinit var databus : DataBus
    @Inject lateinit var domain: Domain

    init {
        CCApplication.appComponent.inject( this )
    }

    fun using( album:Album ) : OpenPresenter {
        this.album = album
        return this
    }

    override fun bind( d:OpenDisplay ) {
        debug( { "OpenPresenter.bind ${d.javaClass.simpleName}" } )

        display = d
        display?.setOpenListener( this )
        display?.setItemCount( photos.size )
        display?.setPosition( firstVisiblePosition )

        if ( disposable == null ) {
            disposable = databus.toObservable()
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe(
                            { data -> onData( data ) },
                            { error -> exception( error ) }
                    )
        }

        if ( photos.isEmpty() ) {
            fetchPhotos( 0,100 )
        }
    }

    override fun unbind() {
        firstVisiblePosition = display?.getFirstVisiblePosition() ?: 0
        display?.release()
        display = null

        debug( { "OpenPresenter.unbind" } )
    }

    override fun release() {
        firstVisiblePosition = 0
        display?.release()
        display = null

        disposable?.dispose()
        disposable = null

        compositeDisposable.clear()
        photos.clear()

        debug( { "OpenPresenter.release" } )
    }

    override fun getDataFor( position:Int ): Photo {
        try {
            return photos[ position ]
        } catch ( x:Exception ) {
            exception( x )
        }
        return Photo()
    }

    override fun getReadyFor( start:Int,count:Int ) {}
    override fun requestRefresh() {}

    override fun onSelectPhoto( photo:Photo ) {}

    private fun fetchPhotos( start:Int,count:Int ) {
        compositeDisposable.clear()

        val observable = domain?.fetchPhotos( album ?: Album(),start,count )
        val disposable = observable
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { next -> run{ onNext( next ) } },
                        { error -> run{ onError( error ) } },
                        { run{ onComplete() } }
                )

        compositeDisposable.add( disposable )
    }

    private fun onNext( photo:Photo ) {
        val indexOf = photos.indexOf( photo )
        when ( indexOf ) {
            -1 -> { photos.add( photo ) }
            else -> { photos[ indexOf ] = photo }
        }
    }

    private fun onComplete() {
        display?.setItemCount( photos.size )
    }

    private fun onError( x:Throwable ) {
        exception( x )
    }

    private fun onData( any:Any? ) {
        // TODO: update photos list from any
    }
}
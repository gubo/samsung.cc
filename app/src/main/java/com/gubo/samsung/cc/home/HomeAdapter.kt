
package com.gubo.samsung.cc.home

import android.net.*
import android.view.*
import android.widget.*
import android.support.v4.widget.*
import android.support.v7.widget.*

import gubo.slipwire.*

import com.gubo.samsung.cc.*
import com.gubo.samsung.cc.model.*
import com.squareup.picasso.Picasso

/**
 * TODO: databinding
 *
 * Created by GUBO on 7/21/2017.
 */
class HomeAdapter( var view:View? ) : HomeDisplay
{
    private var dataSource:DataSource<Album>? = null
    private var recyclerView: RecyclerView? = null
    private var homeListener:HomeListener? = null

    init {
        debug( { "HomeAdapter ${view}" } )

        recyclerView = view?.findViewById( R.id.homerecyclerview ) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager( view?.context,LinearLayoutManager.VERTICAL,false )
        recyclerView?.adapter = AlbumAdapter()
        recyclerView?.setHasFixedSize( true )

        val swipeRefreshLayout = view?.findViewById( R.id.homeswiperefreshlayout ) as SwipeRefreshLayout
        swipeRefreshLayout?.isEnabled = false // TODO: wire up swipeRefreshLayout to call dataSource.requestRefresh

        // TODO: incorporate ReactiveLinearScrollListener for paging
    }

    fun using( dataSource:DataSource<Album> ) : HomeAdapter {
        this.dataSource = dataSource

        val albumAdapter = recyclerView?.adapter as AlbumAdapter
        albumAdapter?.dataSource = this.dataSource

        return this
    }

    override fun setHomeListener( homeListener:HomeListener? ) {
        this.homeListener = homeListener

        val albumAdapter = recyclerView?.adapter as AlbumAdapter
        albumAdapter?.homeListener = this.homeListener
    }

    override fun setItemCount( count:Int ) {
        val albumAdapter = recyclerView?.adapter as AlbumAdapter?
        albumAdapter?.setItemCount( count )
        albumAdapter?.notifyDataSetChanged()
    }

    override fun setPosition( position:Int ) {
        recyclerView?.scrollToPosition( position )
    }

    override fun getFirstVisiblePosition() : Int {
        val linearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager?
        val firstVisiblePosition = linearLayoutManager?.findFirstVisibleItemPosition() ?: 0
        return firstVisiblePosition
    }

    override fun release() {
        homeListener = null
        recyclerView = null
        view = null
        debug( { "HomeAdapter.release" } )
    }

    private class AlbumHolder( view:View ) : RecyclerView.ViewHolder( view )
    {
        private lateinit var albumCardView: CardView
        private lateinit var albumTitleTextView : TextView
        private lateinit var albumImageView: ImageView

        init {
            albumCardView = itemView.findViewById( R.id.albumcardview ) as CardView
            albumTitleTextView = itemView.findViewById( R.id.albumtitletextview ) as TextView
            albumImageView = itemView.findViewById( R.id.albumimageview ) as ImageView
        }

        fun bind( album:Album,homeListener:HomeListener? ) {
            albumCardView.setOnClickListener{ v -> homeListener?.onSelectAlbum( album ) }

            val onLongClickListener = { v:View ->
                homeListener?.onPreviewAlbum( album )
                true
            }
            albumCardView.setOnLongClickListener( onLongClickListener )

            albumTitleTextView.text = album.name

            albumImageView.setImageDrawable( null )
            val uri = Uri.parse( album.photo.url )
            Picasso.with( itemView.context )
                    .load( uri )
                    .fit()
                    .centerInside()
                    .into(albumImageView)
        }
    }

    private class AlbumAdapter : RecyclerView.Adapter<AlbumHolder>()
    {
        private var itemCount = 0

        var dataSource : DataSource<Album>? = null
        var homeListener : HomeListener? = null

        fun setItemCount( count:Int ) {
            this.itemCount = count
        }

        override fun getItemCount(): Int {
            return itemCount
        }

        override fun onCreateViewHolder( parent:ViewGroup?,viewType:Int ) : AlbumHolder {
            val view = LayoutInflater.from( parent?.getContext() ).inflate( R.layout.album,parent,false )
            return AlbumHolder( view )
        }

        override fun onBindViewHolder( holder:AlbumHolder?,position:Int ) {
            val album = dataSource?.getDataFor( position ) ?: Album()
            holder?.bind( album,homeListener )
        }
    }
}
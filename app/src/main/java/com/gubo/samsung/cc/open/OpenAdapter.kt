
package com.gubo.samsung.cc.open

import android.net.*
import android.view.*
import android.widget.*
import android.support.v4.widget.*
import android.support.v7.widget.*

import com.squareup.picasso.*

import gubo.slipwire.*

import com.gubo.samsung.cc.*
import com.gubo.samsung.cc.model.*

/**
 * TODO: databinding
 *
 * Created by GUBO on 7/24/2017.
 */
class OpenAdapter( var view: View? ) : OpenDisplay
{
    private var dataSource: DataSource<Photo>? = null
    private var recyclerView: RecyclerView? = null
    private var openListener: OpenListener? = null

    init {
        debug( { "OpenAdapter ${view}" } )

        recyclerView = view?.findViewById( R.id.openrecyclerview ) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager( view?.context,LinearLayoutManager.VERTICAL,false )
        recyclerView?.adapter = PhotoAdapter()
        recyclerView?.setHasFixedSize( true )

        val swipeRefreshLayout = view?.findViewById( R.id.openswiperefreshlayout ) as SwipeRefreshLayout
        swipeRefreshLayout?.isEnabled = false // TODO: wire up swipeRefreshLayout to call dataSource.requestRefresh

        // TODO: incorporate ReactiveLinearScrollListener for paging
    }

    fun using( dataSource:DataSource<Photo> ) : OpenAdapter {
        this.dataSource = dataSource

        val photoAdapter = recyclerView?.adapter as PhotoAdapter
        photoAdapter?.dataSource = this.dataSource

        return this
    }

    override fun setOpenListener( openListener:OpenListener? ) {
        this.openListener = openListener

        val photoAdapter = recyclerView?.adapter as PhotoAdapter
        photoAdapter?.openListener = this.openListener
    }

    override fun setItemCount( count:Int ) {
        val photoAdapter = recyclerView?.adapter as PhotoAdapter?
        photoAdapter?.setItemCount( count )
        photoAdapter?.notifyDataSetChanged()
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
        openListener = null
        recyclerView = null
        view = null
        debug( { "OpenAdapter.release" } )
    }

    private class PhotoHolder( view:View ) : RecyclerView.ViewHolder( view )
    {
        private lateinit var photoView: View
        private lateinit var photoimageview : ImageView
        private lateinit var photoTitleTextview : TextView

        init {
            photoView = itemView.findViewById( R.id.photoview ) as View
            photoimageview = itemView.findViewById( R.id.photoimageview ) as ImageView
            photoTitleTextview = itemView.findViewById( R.id.phototitletextview ) as TextView
        }

        fun bind( photo:Photo,openListener:OpenListener? ) {
            photoView.setOnClickListener{ v -> openListener?.onSelectPhoto( photo ) }

            photoimageview.setImageDrawable( null )
            val uri = Uri.parse( photo.url )
            Picasso.with( itemView.context )
                    .load( uri )
                    .fit()
                    .centerInside()
                    .into( photoimageview )

            photoTitleTextview.text = photo.name
        }
    }

    private class PhotoAdapter : RecyclerView.Adapter<PhotoHolder>()
    {
        private var itemCount = 0

        var dataSource : DataSource<Photo>? = null
        var openListener : OpenListener? = null

        fun setItemCount( count:Int ) {
            this.itemCount = count
        }

        override fun getItemCount(): Int {
            return itemCount
        }

        override fun onCreateViewHolder( parent:ViewGroup?,viewType:Int ) : PhotoHolder {
            val view = LayoutInflater.from( parent?.getContext() ).inflate( R.layout.photo,parent,false )
            return PhotoHolder( view )
        }

        override fun onBindViewHolder( holder:PhotoHolder?,position:Int ) {
            val photo = dataSource?.getDataFor( position ) ?: Photo()
            holder?.bind( photo,openListener )
        }
    }
}
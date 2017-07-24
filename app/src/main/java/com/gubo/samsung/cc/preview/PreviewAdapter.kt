
package com.gubo.samsung.cc.preview

import android.net.*
import android.view.*
import android.widget.*
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
class PreviewAdapter( var view : View? ) : PreviewDisplay
{
    private var dataSource: DataSource<Photo>? = null
    private var recyclerView: RecyclerView? = null
    private var previewListener: PreviewListener? = null

    init {
        debug( { "PreviewAdapter ${view}" } )

        recyclerView = view?.findViewById( R.id.previewrecyclerview ) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager( view?.context,LinearLayoutManager.HORIZONTAL,false )
        recyclerView?.adapter = PhotoAdapter()
        recyclerView?.setHasFixedSize( true )

        // TODO: incorporate ReactiveLinearScrollListener for paging
    }

    fun using( dataSource:DataSource<Photo> ) : PreviewAdapter {
        this.dataSource = dataSource

        val photoAdapter = recyclerView?.adapter as PhotoAdapter
        photoAdapter?.dataSource = this.dataSource

        return this
    }

    override fun setPreviewListener( previewListener:PreviewListener? ) {
        this.previewListener = previewListener

        val photoAdapter = recyclerView?.adapter as PhotoAdapter
        photoAdapter?.previewListener = this.previewListener
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
        previewListener = null
        recyclerView = null
        view = null
        debug( { "PreviewAdapter.release" } )
    }

    private class PhotoHolder( view:View ) : RecyclerView.ViewHolder( view )
    {
        private lateinit var photoView: View
        private lateinit var photoimageview : ImageView

        init {
            photoView = itemView.findViewById( R.id.previewphotoview ) as View
            photoimageview = itemView.findViewById( R.id.previewphotoimageview ) as ImageView
        }

        fun bind( photo:Photo,previewListener:PreviewListener? ) {
            photoimageview.setImageDrawable( null )
            val uri = Uri.parse( photo.url )
            Picasso.with( itemView.context )
                    .load( uri )
                    .fit()
                    .centerInside()
                    .into( photoimageview )
        }
    }

    private class PhotoAdapter : RecyclerView.Adapter<PhotoHolder>()
    {
        private var itemCount = 0

        var dataSource : DataSource<Photo>? = null
        var previewListener : PreviewListener? = null

        fun setItemCount( count:Int ) {
            this.itemCount = count
        }

        override fun getItemCount(): Int {
            return itemCount
        }

        override fun onCreateViewHolder( parent:ViewGroup?,viewType:Int ) : PhotoHolder {
            val view = LayoutInflater.from( parent?.getContext() ).inflate( R.layout.previewphoto,parent,false )
            return PhotoHolder( view )
        }

        override fun onBindViewHolder( holder:PhotoHolder?,position:Int ) {
            val photo = dataSource?.getDataFor( position ) ?: Photo()
            holder?.bind( photo,previewListener )
        }
    }
}
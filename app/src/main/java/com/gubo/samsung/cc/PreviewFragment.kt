
package com.gubo.samsung.cc

import android.os.*
import android.view.*
import android.support.v4.app.*

import com.gubo.samsung.cc.model.*
import com.gubo.samsung.cc.preview.*

/**
 * Created by GUBO on 7/24/2017.
 */
class PreviewFragment : Fragment()
{
    private val previewPresenter = PreviewPresenter()

    var album: Album? = null

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
    }

    override fun onCreateView( inflater:LayoutInflater?,container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        val view = inflater?.inflate( R.layout.preview,container,false );
        previewPresenter.using( album ?: Album() ).bind( PreviewAdapter( view ).using( previewPresenter ) )
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        previewPresenter.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        previewPresenter.release()
    }
}

fun createPreviewFragment( album:Album ): PreviewFragment {
    val previewFragment = PreviewFragment()
    previewFragment.album = album
    return previewFragment
}

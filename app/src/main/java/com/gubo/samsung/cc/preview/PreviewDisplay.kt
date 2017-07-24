
package com.gubo.samsung.cc.preview

import gubo.slipwire.*

import com.gubo.samsung.cc.model.*

/**
 * Created by GUBO on 7/24/2017.
 */
interface PreviewDisplay : Display,DataSink<Photo>
{
    fun setPreviewListener( previewListener : PreviewListener? )
    fun getFirstVisiblePosition() : Int
}
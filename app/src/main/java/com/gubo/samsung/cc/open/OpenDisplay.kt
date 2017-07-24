
package com.gubo.samsung.cc.open

import gubo.slipwire.*

import com.gubo.samsung.cc.model.*

/**
 * Created by GUBO on 7/24/2017.
 */
interface OpenDisplay : Display,DataSink<Photo>
{
    fun setOpenListener( openListener : OpenListener? )
    fun getFirstVisiblePosition() : Int
}
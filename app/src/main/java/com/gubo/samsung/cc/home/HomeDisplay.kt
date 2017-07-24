
package com.gubo.samsung.cc.home

import gubo.slipwire.*

import com.gubo.samsung.cc.model.*

/**
 * Created by GUBO on 7/21/2017.
 */
interface HomeDisplay : Display,DataSink<Album>
{
    fun setHomeListener( homeListener : HomeListener? )
    fun getFirstVisiblePosition() : Int
}

package com.gubo.samsung.cc.model

/**
 * Created by GUBO on 7/21/2017.
 */
data class Photo(
        val id:String = "",
        val name:String = "",
        val url:String = "",
        val width:Int = 0,
        val height:Int = 0,
        val thumbnailUrl:String = "",
        val thumbnailWidth:Int = 0,
        val thumbnailHeight:Int = 0 )
{
    override fun equals( other:Any? ): Boolean {
        if ( other is Photo ) {
            return ( this.id == other.id )
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

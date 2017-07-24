
package com.gubo.samsung.cc.model

/**
 * Created by GUBO on 7/21/2017.
 */
data class Album(
        val id:String = "",
        val name:String = "",
        val photo:Photo = Photo() )
{
    override fun equals( other:Any? ): Boolean {
        if ( other is Album ) {
            return ( this.id == other.id )
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
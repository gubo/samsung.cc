
package com.gubo.samsung.cc

import io.reactivex.android.schedulers.*

import com.gubo.samsung.cc.model.*
import io.reactivex.schedulers.Schedulers

/**
 * TODO: affect a sync architecture ... have fetch* return from DB, then invoke network call ... on return of network call, update DB and send a type on the DataBus
 *
 * Created by GUBO on 7/21/2017.
 */
class Domain
{
    private val map = HashMap<Album,List<Photo>>()

    private val animals = Album( id="1",name="Animals",photo=Photo( id="1",name="1.jpg",url="file:///android_asset/Animals/1.jpg" ) )
    private val architecture = Album( id="2",name="Architecture",photo=Photo( id="7",name="1.jpg",url="file:///android_asset/Architecture/1.jpg" ) )
    private val food = Album( id="3",name="Food",photo=Photo( id="11",name="1.jpg",url="file:///android_asset/Food/1.jpg" ) )
    private val posters = Album( id="4",name="Posters",photo=Photo( id="16",name="1.jpg",url="file:///android_asset/Posters/1.jpg" ) )
    private val scenery = Album( id="5",name="Scenery",photo=Photo( id="21",name="1.jpg",url="file:///android_asset/Scenery/1.jpg" ) )

    init {
        val _animals = ArrayList<Photo>()
        _animals.add( Photo( id="1",name="1.jpg",url="file:///android_asset/Animals/1.jpg" ) )
        _animals.add( Photo( id="2",name="2.jpg",url="file:///android_asset/Animals/2.jpg" ) )
        _animals.add( Photo( id="3",name="3.jpg",url="file:///android_asset/Animals/3.jpg" ) )
        _animals.add( Photo( id="4",name="4.jpg",url="file:///android_asset/Animals/4.jpg" ) )
        _animals.add( Photo( id="5",name="5.jpg",url="file:///android_asset/Animals/5.jpg" ) )
        _animals.add( Photo( id="6",name="6.jpg",url="file:///android_asset/Animals/6.jpg" ) )
        map.put( animals,_animals )

        val _architecture = ArrayList<Photo>()
        _architecture.add( Photo( id="7",name="1.jpg",url="file:///android_asset/Architecture/1.jpg" ) )
        _architecture.add( Photo( id="8",name="2.jpg",url="file:///android_asset/Architecture/2.jpg" ) )
        _architecture.add( Photo( id="9",name="3.jpg",url="file:///android_asset/Architecture/3.jpg" ) )
        _architecture.add( Photo( id="10",name="4.jpg",url="file:///android_asset/Architecture/4.jpg" ) )
        map.put( architecture,_architecture )

        val _food = ArrayList<Photo>()
        _food.add( Photo( id="11",name="1.jpg",url="file:///android_asset/Food/1.jpg" ) )
        _food.add( Photo( id="12",name="2.jpg",url="file:///android_asset/Food/2.jpg" ) )
        _food.add( Photo( id="13",name="3.jpg",url="file:///android_asset/Food/3.jpg" ) )
        _food.add( Photo( id="14",name="4.jpg",url="file:///android_asset/Food/4.jpg" ) )
        _food.add( Photo( id="15",name="5.jpg",url="file:///android_asset/Food/5.jpg" ) )
        map.put( food,_food )

        val _posters = ArrayList<Photo>()
        _posters.add( Photo( id="16",name="1.jpg",url="file:///android_asset/Posters/1.jpg" ) )
        _posters.add( Photo( id="17",name="2.jpg",url="file:///android_asset/Posters/2.jpg" ) )
        _posters.add( Photo( id="18",name="3.jpg",url="file:///android_asset/Posters/3.jpg" ) )
        _posters.add( Photo( id="19",name="4.jpg",url="file:///android_asset/Posters/4.jpg" ) )
        _posters.add( Photo( id="20",name="5.jpg",url="file:///android_asset/Posters/5.jpg" ) )
        map.put( posters,_posters )

        val _scenery = ArrayList<Photo>()
        _scenery.add( Photo( id="21",name="1.jpg",url="file:///android_asset/Scenery/1.jpg" ) )
        _scenery.add( Photo( id="22",name="2.jpg",url="file:///android_asset/Scenery/2.jpg" ) )
        _scenery.add( Photo( id="23",name="3.jpg",url="file:///android_asset/Scenery/3.jpg" ) )
        _scenery.add( Photo( id="24",name="4.jpg",url="file:///android_asset/Scenery/4.jpg" ) )
        _scenery.add( Photo( id="25",name="5.jpg",url="file:///android_asset/Scenery/5.jpg" ) )
        _scenery.add( Photo( id="26",name="6.jpg",url="file:///android_asset/Scenery/6.jpg" ) )
        map.put( scenery,_scenery )
    }

    /**
     * TODO: affect sync, obey start,count
     */
    fun fetchAlbums( start:Int,count:Int ) : io.reactivex.Observable<Album> {
        val observable = io.reactivex.Observable.fromIterable( listOf( animals,architecture,food,posters,scenery ) )
                .subscribeOn( Schedulers.io() )

        return observable
    }

    /**
     * TODO: affect sync, obey start,count
     */
    fun fetchPhotos( album:Album, start:Int,count:Int ) : io.reactivex.Observable<Photo> {
        val observable = io.reactivex.Observable.fromIterable( map[ album ] ?: listOf() )
                .subscribeOn( Schedulers.io() )

        return observable
    }
}
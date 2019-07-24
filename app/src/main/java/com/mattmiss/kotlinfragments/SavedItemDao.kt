package com.mattmiss.kotlinfragments
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mattmiss.kotlinfragments.SavedItem


//provides queries for getting all words, inserting a word, and deleting all words.

@Dao
interface SavedItemDao{

    @Query("SELECT * from saved_item_table ORDER BY savedItem ASC")
    fun getAllSavedItems(): LiveData<List<SavedItem>>

    @Insert
    suspend fun insert(savedItem: SavedItem)

    @Delete
    suspend fun delete(savedItem: SavedItem)

    @Query("DELETE FROM saved_item_table")
    fun deleteAll()
}
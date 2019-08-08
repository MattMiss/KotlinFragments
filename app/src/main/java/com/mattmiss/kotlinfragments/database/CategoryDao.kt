package com.mattmiss.kotlinfragments.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query



@Dao
interface CategoryDao{

    @Query("SELECT * from category_table ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM category_table")
    fun deleteAll()
}
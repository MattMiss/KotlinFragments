package com.mattmiss.kotlinfragments.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query



@Dao
interface MealDao{

    @Query("SELECT * from meal_table ORDER BY meal ASC")
    fun getAllMeals(): LiveData<List<Meal>>

    @Insert
    suspend fun insert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("DELETE FROM meal_table")
    fun deleteAll()
}
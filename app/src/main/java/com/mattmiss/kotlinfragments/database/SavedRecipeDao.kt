package com.mattmiss.kotlinfragments.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


//provides queries for getting all words, inserting a word, and deleting all words.

@Dao
interface SavedRecipeDao{

    @Query("SELECT * from saved_recipe_table ORDER BY savedRecipe ASC")
    fun getAllSavedRecipes(): LiveData<List<SavedRecipe>>

    @Insert
    suspend fun insert(savedRecipe: SavedRecipe)

    @Delete
    suspend fun delete(savedRecipe: SavedRecipe)

    @Query("DELETE FROM saved_recipe_table")
    fun deleteAll()
}
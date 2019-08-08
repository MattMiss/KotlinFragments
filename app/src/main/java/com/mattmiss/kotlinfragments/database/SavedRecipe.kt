package com.mattmiss.kotlinfragments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_recipe_table")
class SavedRecipe(@PrimaryKey @ColumnInfo(name = "savedRecipe") val savedRecipe: String)
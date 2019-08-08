package com.mattmiss.kotlinfragments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "meal_table")
class Meal(@PrimaryKey @ColumnInfo(name = "meal") val meal: String)
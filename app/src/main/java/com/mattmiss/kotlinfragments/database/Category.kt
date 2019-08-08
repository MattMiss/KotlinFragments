package com.mattmiss.kotlinfragments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category_table")
class Category(@PrimaryKey @ColumnInfo(name = "category") val category: String)
package com.mattmiss.kotlinfragments

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_item_table")
class SavedItem(@PrimaryKey @ColumnInfo(name = "savedItem") val savedItem: String)
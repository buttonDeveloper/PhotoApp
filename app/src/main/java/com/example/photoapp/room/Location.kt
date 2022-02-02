package com.example.photoapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "LOCATION")
class Location {

    @PrimaryKey
    var id: Int = 1

    @ColumnInfo(name = "location_name")
    var location: String = ""
}
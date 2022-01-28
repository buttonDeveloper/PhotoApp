package com.example.photoapp.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable


@Entity(tableName = "LOCATION_TABLE")
class Location {

    @PrimaryKey
    var id: Int = 1

    @ColumnInfo(name = "location")
    var location: String = " "
}
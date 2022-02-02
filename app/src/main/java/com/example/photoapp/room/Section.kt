package com.example.photoapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SECTION")
class Section {

    @PrimaryKey
    var id: Int = 1

    @ColumnInfo(name = "section_name")
    var section: String = ""
}
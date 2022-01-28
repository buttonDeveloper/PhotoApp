package com.example.photoapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SECTION_TABLE")
class Section {

    @PrimaryKey
    var id: Int = 1

    @ColumnInfo(name = "section")
    var section: String = " "
}
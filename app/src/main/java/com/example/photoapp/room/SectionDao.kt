package com.example.photoapp.room

import androidx.room.*

@Dao
interface SectionDao {

    @Query("SELECT * FROM SECTION")
    suspend fun getSection(): Section?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSection(section: Section)

    @Transaction
    suspend fun initSection(section: Section): Section? {
        updateSection(section)
        return getSection()
    }

}
package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DesignDao {
    @Query("SELECT * FROM saved_designs ORDER BY updatedAt DESC")
    fun getAllSavedDesigns(): Flow<List<SavedDesignEntity>>

    @Query("SELECT * FROM saved_designs WHERE id = :id")
    suspend fun getDesignById(id: Long): SavedDesignEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDesign(design: SavedDesignEntity): Long

    @Update
    suspend fun updateDesign(design: SavedDesignEntity)

    @Delete
    suspend fun deleteDesign(design: SavedDesignEntity)

    @Query("DELETE FROM saved_designs WHERE id = :id")
    suspend fun deleteById(id: Long)
}

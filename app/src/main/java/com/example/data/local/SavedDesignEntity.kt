package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.CoverDesignData

@Entity(tableName = "saved_designs")
data class SavedDesignEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val aspectRatio: String,
    val designData: CoverDesignData,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

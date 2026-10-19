package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.CoverDesignData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class DesignConverters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(CoverDesignData::class.java)

    @TypeConverter
    fun fromDesignData(data: CoverDesignData?): String {
        return if (data != null) adapter.toJson(data) else ""
    }

    @TypeConverter
    fun toDesignData(json: String?): CoverDesignData? {
        return if (!json.isNullOrEmpty()) {
            try {
                adapter.fromJson(json)
            } catch (e: Exception) {
                CoverDesignData()
            }
        } else {
            CoverDesignData()
        }
    }
}

package com.devmasterteam.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmasterteam.tasks.service.model.PriorityModel


@Dao
interface PrioriyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(list: List<PriorityModel>)

    @Query("SELECT * FROM Priority")
    fun list():List<PriorityModel>

    @Query("SELECT description FROM Priority WHERE id = :id")
    fun getdescription(id: Int):String

    @Query("DELETE FROM Priority")
    fun clear()
}
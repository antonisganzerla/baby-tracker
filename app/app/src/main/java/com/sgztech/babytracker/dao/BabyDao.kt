package com.sgztech.babytracker.dao

import androidx.room.*
import com.sgztech.babytracker.model.Baby

@Dao
interface BabyDao {

    @Query("SELECT * FROM baby WHERE userId IN (:userId) limit 1")
    suspend fun loadByUserId(userId: Int): Baby?

    @Query("SELECT * FROM baby WHERE userId IN (:userId) and webId is null limit 1")
    suspend fun loadByUserIdWithoutSync(userId: Int): Baby?

    @Query("SELECT EXISTS (SELECT 1 FROM baby WHERE userId=:userId)")
    suspend fun exists(userId: Int): Boolean

    @Insert
    suspend fun insertAll(vararg babies: Baby)

    @Update
    suspend fun update(baby: Baby)

    @Delete
    suspend fun delete(baby: Baby)
}
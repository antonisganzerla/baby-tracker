package com.sgztech.babytracker.dao

import androidx.room.*
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType

@Dao
interface RegisterDao {

    @Query("SELECT * FROM register")
    suspend fun getAll(): List<Register>

    @Query("SELECT * FROM register WHERE userId IN (:userId)")
    suspend fun loadAllByUserId(userId: Int): List<Register>

    @Query("SELECT * FROM register WHERE userId=:userId and type IN (:type)")
    suspend fun loadAllByUserIdAndType(userId: Int, vararg type: RegisterType): List<Register>

    @Query("SELECT EXISTS (SELECT 1 FROM register WHERE userId=:userId)")
    suspend fun exists(userId: Int): Boolean

    @Insert
    suspend fun insertAll(vararg registers: Register)

    @Update
    suspend fun update(register: Register)

    @Delete
    suspend fun delete(register: Register)
}
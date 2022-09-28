package com.sgztech.babytracker.data

import com.sgztech.babytracker.dao.BabyDao
import com.sgztech.babytracker.model.Baby

class BabyRepository(
    private val babyDao: BabyDao,
) {

    suspend fun loadByUserId(userId: Int): Baby? =
        babyDao.loadByUserId(userId)

    suspend fun exists(userId: Int): Boolean =
        babyDao.exists(userId)

    suspend fun insertAll(vararg babies: Baby) {
        babyDao.insertAll(*babies)
    }

    suspend fun update(baby: Baby) {
        babyDao.update(baby)
    }

    suspend fun delete(baby: Baby) {
        babyDao.delete(baby)
    }
}
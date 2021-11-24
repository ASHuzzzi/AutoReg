package ru.lizzzi.autoreg.data.repository

import ru.lizzzi.autoreg.app.App.Companion.applicationContext
import ru.lizzzi.autoreg.data.service.SQLStorage
import ru.lizzzi.autoreg.domain.repository.RegionsRepository

class RegionRepositoryImpl : RegionsRepository {

    private val storage: SQLStorage by lazy {
        SQLStorage(applicationContext())
    }

    init {
        storage.checkDataBase()
    }

    override fun getRegion(codeOfRegion: String): String {
        return storage.getRegion(codeOfRegion)
    }

    override fun getCode(region: String): String {
        return storage.getCode(region)
    }
}
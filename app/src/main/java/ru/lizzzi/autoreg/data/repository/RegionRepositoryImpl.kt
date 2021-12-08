package ru.lizzzi.autoreg.data.repository

import ru.lizzzi.autoreg.app.App.Companion.applicationContext
import ru.lizzzi.autoreg.data.service.SQLStorage
import ru.lizzzi.autoreg.domain.repository.RegionsRepository

class RegionRepositoryImpl : RegionsRepository {

    private val storage: SQLStorage by lazy {
        SQLStorage(applicationContext())
    }

    override fun getRegionCode(codeOfRegion: String): String {
        return storage.getRegionCode(codeOfRegion)
    }

    override fun getRegionCodes(codeOfRegion: String, region: String): String {
        return storage.getRegionCodes(codeOfRegion, region)
    }
}
package ru.lizzzi.autoreg.domain.interactor

import ru.lizzzi.autoreg.data.repository.RegionRepositoryImpl
import ru.lizzzi.autoreg.domain.repository.RegionsRepository

class RegionInteractorImpl: RegionInteractor {

    private val repository: RegionsRepository by lazy {
        RegionRepositoryImpl()
    }

    override fun getRegion(codeOfRegion: String): String {
        return repository.getRegion(codeOfRegion)
    }

    override fun getCode(region: String): String {
        return repository.getCode(region)
    }
}
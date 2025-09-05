package ru.lizzzi.autoreg.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.lizzzi.autoreg.data.repository.RegionRepositoryImpl
import ru.lizzzi.autoreg.domain.entity.Region
import ru.lizzzi.autoreg.domain.repository.RegionsRepository

class RegionInteractorImpl: RegionInteractor {

    private val repository: RegionsRepository by lazy {
        RegionRepositoryImpl()
    }

    override fun getRegion(codeOfRegion: String): Flow<Region> {
        return flow {
            val selectedRegionName = getRegionCode(codeOfRegion)
            val allCodeRegion = getRegionCodes(codeOfRegion, selectedRegionName)
            emit(Region(codeOfRegion, selectedRegionName, allCodeRegion))
        }
    }

    private fun getRegionCode(codeOfRegion: String): String {
        return repository.getRegionCode(codeOfRegion)
    }

    private fun getRegionCodes(codeOfRegion: String, region: String): String {
        return repository.getRegionCodes(codeOfRegion, region)
    }
}
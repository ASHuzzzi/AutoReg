package ru.lizzzi.autoreg.domain.interactor

import io.reactivex.rxjava3.core.Single
import ru.lizzzi.autoreg.data.repository.RegionRepositoryImpl
import ru.lizzzi.autoreg.domain.entity.Region
import ru.lizzzi.autoreg.domain.repository.RegionsRepository

class RegionInteractorImpl: RegionInteractor {

    private val repository: RegionsRepository by lazy {
        RegionRepositoryImpl()
    }

    override fun getRegion(codeOfRegion: String): Single<Region> {
        return Single
            .just(getRegionCode(codeOfRegion))
            .flatMap { selectedRegionName ->
                Single.just(getRegionCodes(codeOfRegion, selectedRegionName)).flatMap { allCodeRegion ->
                    Single.just(
                        Region(codeOfRegion, selectedRegionName, allCodeRegion)
                    )
                }
            }
    }

    private fun getRegionCode(codeOfRegion: String): String {
        return repository.getRegionCode(codeOfRegion)
    }

    private fun getRegionCodes(codeOfRegion: String, region: String): String {
        return repository.getRegionCodes(codeOfRegion, region)
    }
}
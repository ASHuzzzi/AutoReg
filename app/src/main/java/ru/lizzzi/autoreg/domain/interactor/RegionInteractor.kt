package ru.lizzzi.autoreg.domain.interactor

import io.reactivex.rxjava3.core.Single
import ru.lizzzi.autoreg.domain.entity.Region

interface RegionInteractor {

    fun getRegion(codeOfRegion: String): Single<Region>
}
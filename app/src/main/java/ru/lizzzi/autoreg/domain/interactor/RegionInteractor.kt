package ru.lizzzi.autoreg.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.lizzzi.autoreg.domain.entity.Region

interface RegionInteractor {

    fun getRegion(codeOfRegion: String): Flow<Region>
}
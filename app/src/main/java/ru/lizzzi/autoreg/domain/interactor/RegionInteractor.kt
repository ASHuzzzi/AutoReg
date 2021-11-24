package ru.lizzzi.autoreg.domain.interactor

interface RegionInteractor {

    fun getRegion(codeOfRegion: String): String

    fun getCode(region: String): String
}
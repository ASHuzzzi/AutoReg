package ru.lizzzi.autoreg.domain.repository

interface RegionsRepository {

    fun getRegionCode(codeOfRegion: String): String

    fun getRegionCodes(region: String): String
}
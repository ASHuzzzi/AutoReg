package ru.lizzzi.autoreg.domain.repository

interface RegionsRepository {

    fun getRegion(codeOfRegion: String): String

    fun getCode(region: String): String
}
package ru.lizzzi.autoreg

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ViewModelMain(application: Application) : AndroidViewModel(application) {

    private val sqlStorage: SQLStorage by lazy {
        SQLStorage(getApplication<Application>().applicationContext)
    }
    var defaultCodeOfRegion = ""
        private set

    fun checkStorage() {
        sqlStorage.checkDataBase()
    }

    fun getRegion(codeOfRegion: String): String =
        sqlStorage.getRegion(codeOfRegion).also { defaultCodeOfRegion = it }

    fun getOtherCodeOfRegion(region: String): String = sqlStorage.getCode(region)
}
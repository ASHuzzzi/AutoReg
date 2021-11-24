package ru.lizzzi.autoreg.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.lizzzi.autoreg.domain.entity.Region
import ru.lizzzi.autoreg.domain.interactor.RegionInteractor
import ru.lizzzi.autoreg.domain.interactor.RegionInteractorImpl

class ViewModelMain : ViewModel() {

    private val _liveData: MutableLiveData<Region> = MutableLiveData()
    val liveData = _liveData

    private val regionInteractor: RegionInteractor by lazy {
        RegionInteractorImpl()
    }

    fun getRegion(codeOfRegion: String) {
        val selectedRegionName = regionInteractor.getRegion(codeOfRegion)
        val allCodeRegion = getOtherCodeOfRegion(selectedRegionName)
        val region = Region(codeOfRegion, selectedRegionName, allCodeRegion)
        liveData.postValue(region)
    }

    private fun getOtherCodeOfRegion(region: String): String = regionInteractor.getCode(region)
}
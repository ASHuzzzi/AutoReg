package ru.lizzzi.autoreg.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.lizzzi.autoreg.domain.interactor.RegionInteractor
import ru.lizzzi.autoreg.domain.interactor.RegionInteractorImpl

class ViewModelMain : ViewModel() {

    data class ViewState(
        val codeRegion: String = "",
        val regionName: String = "",
        val allCodeRegion: String = ""
    )

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val viewState = _viewState

    private val regionInteractor: RegionInteractor by lazy {
        RegionInteractorImpl()
    }


    init {
        _viewState.value = ViewState()
    }

     fun getRegion(regionCode: String) {
         viewModelScope.launch {
             regionInteractor.getRegion(regionCode).collect { region ->
                 _viewState.value?.let {
                     _viewState.value = it.copy(
                         codeRegion = region.codeRegion,
                         regionName = region.regionName,
                         allCodeRegion = region.allCodeRegion
                     )
                 }
             }
         }
    }
}
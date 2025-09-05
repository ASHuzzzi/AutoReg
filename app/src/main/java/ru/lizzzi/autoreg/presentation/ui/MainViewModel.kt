package ru.lizzzi.autoreg.presentation.ui

import android.app.Application
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.lizzzi.autoreg.R
import ru.lizzzi.autoreg.domain.interactor.RegionInteractor
import ru.lizzzi.autoreg.domain.interactor.RegionInteractorImpl

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _mainScreenState: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState(
        codeRegion = "",
        regionName = "",
        allCodeRegion = ""
    ))
    val viewState = _mainScreenState

    private val regionInteractor: RegionInteractor by lazy {
        RegionInteractorImpl()
    }

    fun checkTextState (textStateValue: String,
    ) {
        if (textStateValue.isEmpty()) {
            showToast(R.string.toastEnterCodeOfRegion)
        } else {
            if (textStateValue.toInt() > 0) {
                getRegion(textStateValue)
            } else {
                showToast(R.string.toastErrorCode)
            }
        }
    }

    private fun getRegion(regionCode: String) {
        viewModelScope.launch {
            regionInteractor.getRegion(regionCode).collect { region ->
                _mainScreenState.value.let {
                    _mainScreenState.value = it.copy(
                        codeRegion = region.codeRegion,
                        regionName = region.regionName,
                        allCodeRegion = region.allCodeRegion
                    )
                }
            }
        }
    }


    private fun showToast(toastTextResource: Int) {
        val toast = Toast.makeText(context,  toastTextResource, Toast.LENGTH_LONG)
        with(toast) {
            this.setGravity(Gravity.CENTER, 0, 0)
            this.show()
        }
    }

}
package ru.lizzzi.autoreg.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.lizzzi.autoreg.domain.interactor.RegionInteractor
import ru.lizzzi.autoreg.domain.interactor.RegionInteractorImpl

class ViewModelMain : ViewModel() {

    data class ViewState(
        val codeRegion: String = "",
        val regionName: String = "",
        val allCodeRegion: String = ""
    )

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    val viewState = _viewState

    private val regionInteractor: RegionInteractor by lazy {
        RegionInteractorImpl()
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    init {
        _viewState.value = ViewState()
    }

    fun getRegion(regionCode: String) {
        regionInteractor.getRegion(regionCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { region ->
                _viewState.value?.let {
                    _viewState.value = it.copy(
                        codeRegion = region.codeRegion,
                        regionName = region.regionName,
                        allCodeRegion = region.allCodeRegion
                    )
                }
            }
            .apply { compositeDisposable.add(this) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
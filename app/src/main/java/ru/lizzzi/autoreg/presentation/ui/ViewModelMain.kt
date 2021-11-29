package ru.lizzzi.autoreg.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.lizzzi.autoreg.domain.entity.Region
import ru.lizzzi.autoreg.domain.interactor.RegionInteractor
import ru.lizzzi.autoreg.domain.interactor.RegionInteractorImpl

class ViewModelMain : ViewModel() {

    private val _liveData: MutableLiveData<Region> = MutableLiveData()
    val liveData = _liveData

    private val regionInteractor: RegionInteractor by lazy {
        RegionInteractorImpl()
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun getRegion(regionCode: String) {
        regionInteractor.getRegion(regionCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { region -> liveData.postValue(region)}
            .apply { compositeDisposable.add(this) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
package com.brustoloni.revoluttestapp.presentation.conversions

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.brustoloni.revoluttestapp.BuildConfig
import com.brustoloni.revoluttestapp.data.model.ConversionsModel
import com.brustoloni.revoluttestapp.data.model.ConversionsUseCases
import com.brustoloni.revoluttestapp.data.model.CurrencysModel
import com.brustoloni.revoluttestapp.data.model.emptyConversionsModel
import com.brustoloni.revoluttestapp.di.SCHEDULER_IO
import com.brustoloni.revoluttestapp.di.SCHEDULER_MAIN_THREAD
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

private val TAG = ConversionsViewModel::class.java.name

class ConversionsViewModel
@Inject constructor(private val conversionsUseCases: ConversionsUseCases, @Named(SCHEDULER_IO) val subscribeOnScheduler:Scheduler, @Named(SCHEDULER_MAIN_THREAD) val observeOnScheduler: Scheduler) : ViewModel() {

    private val log = AnkoLogger(this.javaClass)
    private var currencies: ArrayList<CurrencysModel> = arrayListOf()

    val stateLiveData =  MutableLiveData<ConversionsState>()
    var disposable: Disposable? = null
    private var mBase = ""

    init {
        stateLiveData.value = DefaultState(obtainCurrentData())
    }

    fun updateRecycled(base: String) {
        mBase = base
        disposable = Observable.interval(
            0, 1, TimeUnit.SECONDS)
            .observeOn(observeOnScheduler)
            .subscribe(this::getConversions, this::onError)
    }

    fun updateBase(base: String) {
        mBase = base
    }

    fun getCoonvertToArrayList(): ArrayList<CurrencysModel> {
        return currencies
    }

    //region private methods
    @SuppressLint("CheckResult")
    private fun getConversions(long: Long) {
        conversionsUseCases.getConversions(mBase)
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribe(this::onSuccess, this::onError)

    }

    private fun onSuccess(conversions: ConversionsModel) {
        convertToArrayList(conversions)
        stateLiveData.value = DefaultState(conversions)
    }

    private fun onError(error: Throwable) {
        log.warn { error.message }
        stateLiveData.value = ErrorState(error.message ?: "",  obtainCurrentData())
    }

    private fun convertToArrayList(conversions: ConversionsModel): ArrayList<CurrencysModel> {
        currencies = arrayListOf()
        var model = CurrencysModel(conversions.base, 1.0f)
        currencies.add(model)
        conversions.rates.forEach { (key, value) ->
            model = CurrencysModel(key, value)
            currencies.add(model)
        }
        return currencies
    }

    private fun obtainCurrentData() = stateLiveData.value?.conversions ?: emptyConversionsModel
    //endregion
}
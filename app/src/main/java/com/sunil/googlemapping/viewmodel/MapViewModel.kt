package com.sunil.googlemapping.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunil.googlemapping.model.MapModel
import com.sunil.googlemapping.repository.getModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MapViewModel() : ViewModel() {

    private var dashBoardLiveData = MutableLiveData<MapModel>()

    /*
    * hold data through live data holder
    * */
    val holdLiveData: LiveData<MapModel> get() = dashBoardLiveData

    /*
    * @viewModelScope define scope the work to the ViewModel so that if the ViewModel is cleared,
    *  the work is canceled automatically to avoid consuming resources.
    * */
    fun getDashBoard(context: Context) {
        viewModelScope.launch {
            dashBoardLiveData.value =
                getModel(context)
        }
    }
}
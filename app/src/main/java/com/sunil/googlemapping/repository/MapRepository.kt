package com.sunil.googlemapping.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunil.googlemapping.model.MapModel

/*
* this repository provide data from asset hub.json
* */
fun getModel(context: Context): MapModel {
    val jsonFileString = getJsonDataFromAsset(
        context,
        "hub.json"
    )
    val gson = Gson()
    val mapModelType = object : TypeToken<MapModel>() {}.type
    val mapModel: MapModel = gson.fromJson(jsonFileString, mapModelType)
    return mapModel;
}

fun getJsonDataFromAsset(applicationContext: Context?, fileName: String): String {
    return applicationContext?.assets?.open(fileName)?.bufferedReader().use { it?.readText() }!!
}
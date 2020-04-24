package com.sunil.googlemapping.model

data class MapModel(
    val results: Results
)

data class Results(
    val bounds: Bounds,
    val geometry: List<List<Geometry>>,
    val id: Int,
    val tags: Tags,
    val type: String
)

data class Bounds(
    val maxlat: Double,
    val maxlon: Double,
    val minlat: Double,
    val minlon: Double
)

data class Geometry(
    val lat: Double,
    val lon: Double
)

data class Tags(
    val name: String
)
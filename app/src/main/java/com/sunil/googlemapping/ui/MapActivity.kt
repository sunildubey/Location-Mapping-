package com.sunil.googlemapping

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.PolyUtil
import com.sunil.googlemapping.model.Geometry
import com.sunil.googlemapping.model.MapModel
import com.sunil.googlemapping.model.UserInputModel
import com.sunil.googlemapping.ui.OutputActivity
import com.sunil.googlemapping.viewmodel.MapViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    var googleMap: GoogleMap? = null
    lateinit var showData: MapModel
    private lateinit var mapFragment: SupportMapFragment
    private val listOfLocation = arrayListOf<LatLng>()
    private val viewModel: MapViewModel by viewModel()
    private lateinit var fab: FloatingActionButton
    val userInputList = arrayListOf<UserInputModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // set resource
        initView()
        // load json data
        loadData()
        fab.setOnClickListener {
            val intent = Intent(this, OutputActivity::class.java)
            intent.putExtra("output", userInputList)
            startActivity(intent)
        }
    }

    /*
    * set map view
    * */
    private fun initView() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fab = findViewById(R.id.fab)
    }

    private fun loadData() {

        /*
        * observer emitting data
        * */
        viewModel.holdLiveData.observe(this, Observer {
            showData = it
            Log.e(TAG, showData.toString())
        })

        viewModel.getDashBoard(this)
    }

    companion object {
        private val TAG = MapActivity::class.java.simpleName
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        Log.e("load ", showData.toString())

        /*
        * set bounds @LatLngBounds.Builder() on map
        * */
        val builder = LatLngBounds.Builder()
        builder.include(LatLng(showData.results.bounds.maxlat, showData.results.bounds.maxlon))
        builder.include(LatLng(showData.results.bounds.minlat, showData.results.bounds.minlon))
        val bounds = builder.build()
        bounds.center

        /*
        * set polygon area
        * */
        val options = PolygonOptions()
        val geometry: List<List<Geometry>> = showData.results.geometry
        val geometryData: List<Geometry> = geometry[0]
        for (coordinate in geometryData) {
            val point = LatLng(coordinate.lat, coordinate.lon)
            listOfLocation.add(point)
            options.add(point)
        }

        /*
        * set polygon on Map
        * */
        val polygon: Polygon = googleMap!!.addPolygon(options)
        polygon.fillColor = 0x7FDC740D
        polygon.strokeColor = Color.parseColor("#FF7F00")

        val padding = 100 // offset from edges of the map in pixels
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap!!.animateCamera(cameraUpdate)

        /*
        * add mock user input location
        * */
        userInputList.add(UserInputModel(50.840473, -0.146755, 30,false))
        userInputList.add(UserInputModel(50.842458, -0.150285, 5,false))
        userInputList.add(UserInputModel(50.843317, -0.144960, 0,false))
        userInputList.add(UserInputModel(44.197736, 1.183339, 1000000,false))
        userInputList.add(UserInputModel(50.854067, -0.163824, 100,false))

        /*
        * iterate input location
        * */
        for (userInput in userInputList) {
            val returnStatus =
                isLocationWithinArea(userInput.userLat, userInput.userLon, userInput.accuracy)
            userInput.status = returnStatus
            Log.e("Status ", "" + returnStatus)
        }
        Log.e("changes  ", userInputList.toString())

    }


    private fun isLocationWithinArea(lat: Double, long: Double, accuracy: Int): Boolean {

        var distance = 0.0
        val userInputLocation = LatLng(lat, long)

        /*
        * check input location inside polygon
        * */
        val isInside = PolyUtil.containsLocation(lat, long, listOfLocation, true)

        /*
        * nearest point from all geometry location
        * */
        val nearestPoint = findNearestPoint(userInputLocation, listOfLocation)

        /*
        * user input location
        * */
        val userLocation = Location("")
        userLocation.accuracy = accuracy.toFloat();
        userLocation.latitude = userInputLocation.latitude
        userLocation.longitude = userInputLocation.longitude

        /*
        * nearest location
        * */
        val nearestLocation = Location("")
        nearestLocation.latitude = nearestPoint!!.latitude
        nearestLocation.longitude = nearestPoint.longitude

        /*
        * calculate distance
        * */
        distance = ((userLocation.distanceTo(nearestLocation) - accuracy).toDouble())
        Log.e("Distance :: ", "" + distance)

        /*
        * return status of user input location @within location range or calculated distance should
        *  be less than @accuracy
        *
        * */
        return isInside || distance < accuracy
    }

    /*
    * get nearest point from user input location
    * */
    private fun findNearestPoint(test: LatLng?, target: List<LatLng>?): LatLng? {
        var distance = -1.0
        var minimumDistancePoint = test
        if (test == null || target == null) {
            return minimumDistancePoint
        }
        for (i in target.indices) {
            val point = target[i]
            var segmentPoint = i + 1
            if (segmentPoint >= target.size) {
                segmentPoint = 0
            }
            val currentDistance =
                PolyUtil.distanceToLine(test, point, target[segmentPoint])
            if (distance == -1.0 || currentDistance < distance) {
                distance = currentDistance
                minimumDistancePoint = findNearestPoint(test, point, target[segmentPoint])
            }
        }
        return minimumDistancePoint
    }


    private fun findNearestPoint(p: LatLng, start: LatLng, end: LatLng): LatLng? {
        if (start == end) {
            return start
        }
        val s0lat = Math.toRadians(p.latitude)
        val s0lng = Math.toRadians(p.longitude)
        val s1lat = Math.toRadians(start.latitude)
        val s1lng = Math.toRadians(start.longitude)
        val s2lat = Math.toRadians(end.latitude)
        val s2lng = Math.toRadians(end.longitude)
        val s2s1lat = s2lat - s1lat
        val s2s1lng = s2lng - s1lng
        val u = (((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng))
        if (u <= 0) {
            return start
        }
        return if (u >= 1) {
            end
        } else LatLng(
            start.latitude + u * (end.latitude - start.latitude),
            start.longitude + u * (end.longitude - start.longitude)
        )
    }
}
package com.sunil.googlemapping.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.sunil.googlemapping.R
import com.sunil.googlemapping.model.UserInputModel
import com.sunil.googlemapping.utilities.VerticalSpaceItemDecoration

class OutputActivity : AppCompatActivity() {

    private var outputAdapter: OutputAdapter? = null

    private lateinit var mToolbarTitle: TextView
    private lateinit var mToolbar: Toolbar
    private lateinit var mAppBarlayout: AppBarLayout
    private lateinit var mRecycler: RecyclerView
    var userInputList = arrayListOf<UserInputModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_output)
        userInputList = intent.extras?.get("output") as ArrayList<UserInputModel>
        Log.e(TAG, userInputList.toString())
        initView()
        //load output
        loadData()
    }


    private fun initView() {
        mToolbarTitle = findViewById(R.id.toolbarTitle)
       mToolbarTitle.text = getString(R.string.Output)
        mToolbar = findViewById(R.id.toolbar)
        mAppBarlayout = findViewById(R.id.appBarlayout)
        mRecycler = findViewById(R.id.recycler)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecycler.layoutManager = linearLayoutManager
        mRecycler.addItemDecoration(VerticalSpaceItemDecoration(this))
    }

    private fun loadData() {
        outputAdapter = OutputAdapter(this, userInputList)
        mRecycler.adapter = outputAdapter
        outputAdapter?.notifyDataSetChanged()
    }

    companion object {
        private val TAG = OutputActivity::class.java.simpleName
    }
}
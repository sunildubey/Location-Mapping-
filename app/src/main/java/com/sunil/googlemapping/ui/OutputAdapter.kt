package com.sunil.googlemapping.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunil.googlemapping.R
import com.sunil.googlemapping.model.UserInputModel

class OutputAdapter(
    private val context: Context,
    private val outputList: ArrayList<UserInputModel>
) : RecyclerView.Adapter<OutputAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = R.layout.item_output
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (position < outputList.size) {
                holder.mTxtlat.text = outputList[position].userLat.toString()
                holder.mTxtlon.text = outputList[position].userLon.toString()
                holder.mTxtAccuracy.text = outputList[position].accuracy.toString()
                holder.mTxtOutput.text = outputList[position].status.toString()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in onBindViewHolder", e)
        }
    }

    override fun getItemCount(): Int {
        return outputList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mLlParent: LinearLayout = itemView.findViewById(R.id.llParent)
        var mTxtlat: TextView = itemView.findViewById(R.id.txtlat)
        var mTxtlon: TextView = itemView.findViewById(R.id.txtlon)
        var mTxtAccuracy: TextView = itemView.findViewById(R.id.txtAccuracy)
        var mTxtOutput: TextView = itemView.findViewById(R.id.txtOutput)
    }

    companion object {
        private val TAG = OutputAdapter::class.java.simpleName
    }
}


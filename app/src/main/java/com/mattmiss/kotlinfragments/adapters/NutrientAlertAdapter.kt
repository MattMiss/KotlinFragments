package com.mattmiss.kotlinfragments.adapters

import com.mattmiss.kotlinfragments.R
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.nutrient_alert_item.*
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.inflate
import kotlinx.android.synthetic.main.nutrient_alert_item.view.*


class NutrientAlertAdapter(private val alerts : ArrayList<String>) : RecyclerView.Adapter<NutrientAlertAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.nutrient_alert_item, false)

        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alert = alerts[position]

        holder.bindFood(alert)
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

        }

        fun bindFood(alert : String){
            itemView.alertName.text = alert
        }
    }
}
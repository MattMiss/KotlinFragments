package com.mattmiss.kotlinfragments

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.listview_item.view.*
import org.json.JSONObject

class SearchResultsAdapter(private val foodItem : ArrayList<JSONObject>, val clickListener: (JSONObject) -> Unit): RecyclerView.Adapter<SearchResultsAdapter.FoodHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val inflatedView = parent.inflate(R.layout.listview_item, false)
        return FoodHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return foodItem.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val foodSave = foodItem[position]

        holder.bindFood(foodSave, clickListener)
    }

    //1
    class FoodHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var foodSave: JSONObject? = null

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
           // Log.d("RecyclerView", "CLICK!")

        }

        fun bindFood(foodSave: JSONObject, clickListener: (JSONObject) -> Unit) {
            this.foodSave = foodSave

            view.labelName.text = foodSave.getString("food_name")
            view.labelDescription.text = foodSave.getString("food_type")
            // Set the click listener to the listener provided in the constructor
            view.setOnClickListener { clickListener(foodSave) }

        }

        companion object {
            //5
            private val FOOD_KEY = "FOOD"
        }
    }
}
package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.R
import kotlinx.android.synthetic.main.food_or_recipe_item.view.*
import org.json.JSONObject

class MealListAdapter internal constructor(context: Context, val clickListener: (JSONObject) -> Unit) :
    RecyclerView.Adapter<MealListAdapter.MealItemHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mealsList = emptyList<JSONObject>() // Cached copy of words



    inner class MealItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {

        }
        private var view: View = itemView
        private var meal: JSONObject? = null

        //val savedItemView: TextView = itemView.findViewById(R.id.textView)


        fun bindSavedItem(meal: JSONObject, clickListener: (JSONObject) -> Unit){
            this.meal = meal


            view.setOnClickListener { clickListener(meal) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealItemHolder {
        val itemView = inflater.inflate(R.layout.meal_list_item, parent, false)

        return MealItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealItemHolder, position: Int) {
        val current = mealsList[position]
        val currentJSON = current

        holder.bindSavedItem(currentJSON, clickListener)
    }

    internal fun setSavedItems(mealsList: List<JSONObject>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun getItemCount() = mealsList.size



}
package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.R
import kotlinx.android.synthetic.main.food_or_recipe_item.view.*
import org.json.JSONObject

class ChosenMealsListAdapter internal constructor(context: Context, val clickListener: (JSONObject) -> Unit) :
    RecyclerView.Adapter<ChosenMealsListAdapter.SavedItemHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var savedItems = emptyList<JSONObject>() // Cached copy of words



    inner class SavedItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {

        }
        private var view: View = itemView
        private var savedItem: JSONObject? = null

        //val savedItemView: TextView = itemView.findViewById(R.id.textView)


        fun bindSavedItem(savedItem: JSONObject, clickListener: (JSONObject) -> Unit){
            this.savedItem = savedItem
            var foodOrRecipe = ""

            if (savedItem.toString().contains("recipe")){
                view.labelName.text = savedItem.getString("recipe_name")
                //view.labelDescription.text = savedItem.getString("recipe_description")
                view.icon.setImageResource(R.drawable.ic_recipeicon)

                foodOrRecipe = "recipe"
            }else {
                view.labelName.text = savedItem.getString("food_name")
                //view.labelDescription.text = savedItem.getString("food_type")

                if (savedItem.toString().contains("brand")){
                    view.icon.setImageResource(R.drawable.ic_brandicon)
                }
                foodOrRecipe = "food"
            }
            val servingAmount = savedItem.getDouble("serving_amount").toString()
            val servingType = savedItem.getString("serving_type")

            val tempAmounts = servingAmount.split(".")
            val wholeNum = tempAmounts[0]
            val fracNum = when(tempAmounts[1]){
                "125" -> "1/8"
                "25" -> "1/4"
                "375" -> "3/8"
                "5" -> "1/2"
                "625" -> "5/8"
                "75" -> "3/4"
                "875" -> "7/8"
                else -> " "
            }

            view.labelDescription.text = "$wholeNum $fracNum $servingType"

            view.setOnClickListener { clickListener(savedItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedItemHolder {
        val itemView = inflater.inflate(R.layout.food_or_recipe_item, parent, false)
        return SavedItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedItemHolder, position: Int) {
        val current = savedItems[position]
        val currentJSON = current

        holder.bindSavedItem(currentJSON, clickListener)
        //holder.savedItemView.text = currentJSON.getString("food_name")
    }

    internal fun setSavedItems(savedItems: List<JSONObject>) {
        this.savedItems = savedItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = savedItems.size



}
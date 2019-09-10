package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.R
import kotlinx.android.synthetic.main.food_or_recipe_item.view.*
import org.json.JSONObject

class SavedItemListAdapter internal constructor(context: Context, val clickListener: (JSONObject, Int) -> Unit) :
    RecyclerView.Adapter<SavedItemListAdapter.SavedItemHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var savedItems = emptyList<JSONObject>() // Cached copy of words



    inner class SavedItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {

        }
        private var view: View = itemView
        private var savedItem: JSONObject? = null

        //val savedItemView: TextView = itemView.findViewById(R.id.textView)


        fun bindSavedItem(savedItem: JSONObject, clickListener: (JSONObject, Int) -> Unit){
            this.savedItem = savedItem
            var foodOrRecipe = ""

            if (savedItem.toString().contains("recipe")){
                view.labelName.text = savedItem.getString("recipe_name")
                view.labelDescription.text = savedItem.getString("recipe_description")
                view.icon.setImageResource(R.drawable.ic_recipeicon)

                view.setOnClickListener { clickListener(savedItem, RECIPE_ID) }
            }else {
                view.labelName.text = savedItem.getString("food_name")
                view.labelDescription.visibility = View.GONE

                if (savedItem.toString().contains("brand")){
                    view.icon.setImageResource(R.drawable.ic_brandicon)
                    view.labelDescription.visibility = View.VISIBLE
                    view.labelDescription.text = savedItem.getString("brand_name")
                }
                view.setOnClickListener { clickListener(savedItem, FOOD_ID) }
            }



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

    companion object{
        val FOOD_ID = 1
        val RECIPE_ID = 2
    }

}
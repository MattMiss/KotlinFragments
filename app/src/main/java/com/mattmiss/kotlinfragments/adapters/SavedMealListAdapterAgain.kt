package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.R
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.food_or_recipe_item.view.*
import org.json.JSONObject

class SavedMealListAdapterAgain internal constructor(context: Context, val clickListener: (JSONObject) -> Unit) :
    SectionedRecyclerViewAdapter<SavedMealListAdapterAgain.SubheaderViewHolder, SavedMealListAdapterAgain.ItemViewHolder>(){
    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindSubheaderViewHolder(p0: SubheaderViewHolder?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemSize(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateItemViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {

        val itemView = inflater.inflate(R.layout.food_or_recipe_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup?, viewType: Int): SubheaderViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindItemViewHolder(holder: ItemViewHolder?, position: Int) {
        val current = savedItems[position]
        val currentJSON = current

        holder?.bindSavedItem(currentJSON, clickListener)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var savedItems = emptyList<JSONObject>() // Cached copy of words



    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {

        }
        private var view: View = itemView
        private var savedItem: JSONObject? = null

        //val savedItemView: TextView = itemView.findViewById(R.id.textView)


        fun bindSavedItem(savedItem: JSONObject, clickListener: (JSONObject) -> Unit){
            this.savedItem = savedItem

            view.labelName.text = savedItem.getString("date")

            val mealArray = savedItem.getJSONArray("all_meals")

            view.labelDescription.text = "${mealArray.length()} Meals Total"

            view.setOnClickListener { clickListener(savedItem) }
        }
    }

    inner class SubheaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }



    internal fun setSavedItems(savedItems: List<JSONObject>) {
        this.savedItems = savedItems
        notifyDataSetChanged()
    }





}
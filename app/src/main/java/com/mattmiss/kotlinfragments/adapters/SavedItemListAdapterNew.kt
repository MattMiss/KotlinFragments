package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.R
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.food_or_recipe_item.view.*
import org.json.JSONObject

class SavedItemListAdapterNew internal constructor(context: Context, val clickListener: (JSONObject, Int) -> Unit) :
    SectionedRecyclerViewAdapter<SavedItemListAdapterNew.SubheaderViewHolder, SavedItemListAdapterNew.ItemViewHolder>(){


    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        return false
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var savedItems = emptyList<JSONObject>() // Cached copy of words

    private val TAG = SavedItemListAdapterNew::class.java.name


    @CallSuper
    override fun onBindSubheaderViewHolder(p0: SubheaderViewHolder?, p1: Int) {

    }

    override fun getItemSize(): Int {
        Log.i(TAG, "ItemSize =  ${savedItems.size}")
        return savedItems.size
    }

    override fun onCreateItemViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.food_or_recipe_item, parent, false)
        return ItemViewHolder(itemView)

    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup?, viewType: Int): SubheaderViewHolder {
        val itemView = inflater.inflate(R.layout.food_or_recipe_item, parent, false)
        return SubheaderViewHolder(itemView)
    }

    override fun onBindItemViewHolder(holder: ItemViewHolder?, position: Int) {
        val current = savedItems[position]
        val currentJSON = current

        Log.i(TAG, "Binding $position")

        holder?.bindSavedItem(currentJSON, clickListener)
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {

        }
        private var view: View = itemView
        private var savedItem: JSONObject? = null

        //val savedItemView: TextView = itemView.findViewById(R.id.textView)


        fun bindSavedItem(savedItem: JSONObject, clickListener: (JSONObject, Int) -> Unit){
            this.savedItem = savedItem

            Log.i(TAG, "Bind saved item: ${savedItem}")

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

    inner class SubheaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    internal fun setSavedItems(savedList: List<JSONObject>) {
        Log.i(TAG, "Saved Items used in this adapter: ${savedList}")
        savedItems = savedList
        notifyDataSetChanged()
        Log.i(TAG, "Saved Items in the list: ${savedItems}")
    }

    companion object{
        val FOOD_ID = 1
        val RECIPE_ID = 2

    }

}
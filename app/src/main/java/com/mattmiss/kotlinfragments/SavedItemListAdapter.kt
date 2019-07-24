package com.mattmiss.kotlinfragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.SavedItem
import kotlinx.android.synthetic.main.listview_item.view.*
import org.json.JSONObject

class SavedItemListAdapter internal constructor(context: Context, val clickListener: (JSONObject) -> Unit) :
    RecyclerView.Adapter<SavedItemListAdapter.SavedItemHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var savedItems = emptyList<SavedItem>() // Cached copy of words



    inner class SavedItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        private var view: View = itemView
        private var savedItem: JSONObject? = null

        //val savedItemView: TextView = itemView.findViewById(R.id.textView)

        init {
            view.setOnClickListener(this)
        }

        fun bindSavedItem(savedItem: JSONObject, clickListener: (JSONObject) -> Unit){
            this.savedItem = savedItem

            view.labelName.text = savedItem.getString("food_name")
            view.labelDescription.text = savedItem.getString("food_type")
            view.setOnClickListener { clickListener(savedItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedItemHolder {
        val itemView = inflater.inflate(R.layout.listview_item, parent, false)
        return SavedItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedItemHolder, position: Int) {
        val current = savedItems[position]
        val currentJSON = JSONObject(current.savedItem)

        holder.bindSavedItem(currentJSON, clickListener)
        //holder.savedItemView.text = currentJSON.getString("food_name")
    }

    internal fun setSavedItems(savedItems: List<SavedItem>) {
        this.savedItems = savedItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = savedItems.size



}
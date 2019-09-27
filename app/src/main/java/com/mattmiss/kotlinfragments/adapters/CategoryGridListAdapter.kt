package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.Category
import kotlinx.android.synthetic.main.category_spinner.view.*

class CategoryGridListAdapter(context: Context,
                       categories: List<Category>) :
    ArrayAdapter<Category>(context, 0, categories) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val category = (getItem(position)) as Category
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.category_spinner,
            parent,
            false
        )
        view.beefImage.setImageResource(category.image)
        view.beefName.text = category.name

        return this.createView(position, view, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val category = (getItem(position)) as Category
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.category_spinner,
            parent,
            false
        )
        view.beefImage.setImageResource(category.image)
        view.beefName.text = category.name
        return view
    }
}
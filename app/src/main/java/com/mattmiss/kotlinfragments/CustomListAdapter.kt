package com.mattmiss.kotlinfragments

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomNoteListAdapter(var mCtx: Context, var resource:Int, var notes:List<String>)
    :ArrayAdapter<String>( mCtx , resource , notes ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater :LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )

        var textView : TextView = view.findViewById(R.id.label)


        var note = notes[position]


        textView.text = note



        return view
    }

}
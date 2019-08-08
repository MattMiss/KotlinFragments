package com.mattmiss.kotlinfragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.WindowManager
import com.mattmiss.kotlinfragments.R
import org.json.JSONObject


class CustomListAdapter(var mCtx: Context, var resource:Int, var notes:List<String>, var type : String)
    :ArrayAdapter<String>( mCtx , resource , notes ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater :LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )

        val textView : TextView = view.findViewById(R.id.label)


        val note = notes[position]

        textView.text = note

        if (type.equals("direction")){
            val lblDirectionNum : TextView = view.findViewById(R.id.lblNumber)

            lblDirectionNum.text = "${position + 1}"
        }

        if (type.equals("ingredient")){

            val tempJSON = JSONObject(notes[position])
            val description = tempJSON.get("ingredient_description").toString()

            val lblDirectionNum : TextView = view.findViewById(R.id.lblNumber)
            val lblText : TextView = view.findViewById(R.id.label)

            lblDirectionNum.text = "${position + 1}"
            lblText.text = description

            setItemHeight(description, textView)
        }

        if (type.equals("direction")){

            val tempJSON = JSONObject(notes[position])
            val description = tempJSON.get("direction_description").toString()

            val lblDirectionNum : TextView = view.findViewById(R.id.lblNumber)
            val lblText : TextView = view.findViewById(R.id.label)

            lblDirectionNum.text = "${position + 1}"
            lblText.text = description

            setItemHeight(description, textView)
        }

        return view
    }

    fun setItemHeight(listItemText : String, textView : TextView){
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val screenWidth = display.width // Get full screen width
        val eightyPercent = screenWidth * 80 / 100 // Calculate 80% of it
        // as my adapter was having almost 80% of the whole screen width

        val textWidth = textView.paint.measureText(listItemText)
        // this method will give you the total width required to display total String


        val numberOfLines = textWidth.toInt() / eightyPercent + 1
        // calculate number of lines it might take

        textView.setLines(numberOfLines)
    }


}
package com.mattmiss.kotlinfragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONException
import org.json.JSONObject

class FoodPagerAdapter(manager: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {
    var foodItem : String? = null


    override fun getItem(position: Int) : Fragment {


        val returnFragment = SavedResultFragment.newInstance(foodItem!!, position)

        return returnFragment

    }

    override fun getCount(): Int {
        // Returns length of foodItem Servings Array
        var count = 0

        try{
            count = JSONObject(foodItem).getJSONObject("servings").getJSONArray("serving").length()
        }catch (je: JSONException){
            count = 1
        }


        return count
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Returns Title depending on position
        var title = ""
        val foodItemJSON = JSONObject(foodItem)

        /*
        // Use if I need to include total serving amount
        try {
            title = "[${position + 1} of ${foodItemJSON.getJSONObject("servings")
                .getJSONArray("serving").length()}] Servings"
        }catch (je: JSONException) {
            title = "${position + 1} of 1 Serving"
        }
        */

        title = "- ${position + 1} -"

        return title
    }

    fun setJSONObject(foodJSON : JSONObject){
       foodItem = foodJSON.toString()
    }
}

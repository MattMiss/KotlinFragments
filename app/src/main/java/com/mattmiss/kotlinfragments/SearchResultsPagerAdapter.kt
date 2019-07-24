package com.mattmiss.kotlinfragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONException
import org.json.JSONObject

class SearchResultsPagerAdapter(manager: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {
    var foodItem : String? = null
    val SERVINGS_STRING = "servings"
    var instanceCode : Int? = null

    override fun getItem(position: Int) : Fragment {

        var returnFragment : Fragment

        if (instanceCode == 1){
            returnFragment = SearchResultFragment.newInstance(foodItem!!, position)
        }else{
            returnFragment = SavedResultFragment.newInstance(foodItem!!, position)
        }

        return returnFragment

    }

    override fun getCount(): Int {
        // Returns length of foodItem Servings Array
        var count = 0

        val containsServings = foodItem!!.contains(SERVINGS_STRING, ignoreCase = true)

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

        try {
            title = "[${position + 1} of ${foodItemJSON.getJSONObject("servings")
                .getJSONArray("serving").length()}] Servings"
        }catch (je: JSONException) {
            title = "${position + 1} of 1 Serving"
        }

        return title
    }

    fun setJSONObject(instanceCode: Int, foodJSON : JSONObject){
       foodItem = foodJSON.toString()
       this.instanceCode = instanceCode

    }
}

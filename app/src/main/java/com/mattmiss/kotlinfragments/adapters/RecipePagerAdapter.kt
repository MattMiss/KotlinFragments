package com.mattmiss.kotlinfragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mattmiss.kotlinfragments.fragments.SearchRecipeResultFragment
import org.json.JSONException
import org.json.JSONObject

class RecipePagerAdapter(manager: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {
    var recipeItem : String? = null

    override fun getItem(position: Int) : Fragment {

        val returnFragment = SearchRecipeResultFragment.newInstance(recipeItem!!, position)

        return returnFragment

    }

    override fun getCount(): Int {
        // Replace this later (7.30.19)
        var count = 1

        try{
            //count = JSONObject(foodItem).getJSONObject("servings").getJSONArray("serving").length()
        }catch (je: JSONException){
            //count = 1
        }


        return count
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Returns Title depending on position
        var title = ""
        val recipeItemJSON = JSONObject(recipeItem)

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

    
    fun setJSONObject(recipeJSON : JSONObject){
       recipeItem = recipeJSON.toString()
    }
}

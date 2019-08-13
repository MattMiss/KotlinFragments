package com.mattmiss.kotlinfragments.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mattmiss.kotlinfragments.adapters.FoodPagerAdapter
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.adapters.RecipePagerAdapter
import kotlinx.android.synthetic.main.pager_tabs.*
import org.json.JSONObject


class SearchResultViewPagerParent : androidx.fragment.app.DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.pager_tabs, container)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodOrRecipeJSON = JSONObject(arguments?.getString("foodOrRecipeJSON"))
        val resultCode = arguments?.getInt("resultCode")


        if (resultCode == 1){
            foodTitleText.text = foodOrRecipeJSON.get("food_name").toString()

            val adapter = FoodPagerAdapter(childFragmentManager)
            adapter.setJSONObject(foodOrRecipeJSON)
            pager.adapter = adapter

        }else if (resultCode == 2){
            foodTitleText.text = foodOrRecipeJSON.get("recipe_name").toString()

            println("TESTTTTTT")

            val servingsJSON = JSONObject(foodOrRecipeJSON.get("serving_sizes").toString()).get("serving").toString()

            println(servingsJSON)



            val adapter = RecipePagerAdapter(childFragmentManager)
            adapter.setJSONObject(foodOrRecipeJSON)
            pager.adapter = adapter
        }



        btnCancelFood.setOnClickListener{
            dismiss()
        }

        btnSaveFood.setOnClickListener{
            saveFoodItem(foodOrRecipeJSON)
        }
    }


    private fun saveFoodItem(foodSave : JSONObject) {
        val ft = activity!!.supportFragmentManager.beginTransaction()

        val resultCode = arguments?.getInt("resultCode")

        if (resultCode == 1){
            val newFragment = AddNotesBeforeFoodSave.newInstance(foodSave)
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
            ft.addToBackStack("fragment_foodSave_dialog")
            newFragment.show(ft, "fragment_foodSave_dialog")
        }else if(resultCode == 2){
            val newFragment = AddNotesBeforeRecipeSave.newInstance(foodSave)
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
            ft.addToBackStack("fragment_recipeSave_dialog")
            newFragment.show(ft, "fragment_recipeSave_dialog")
        }


    }


    companion object {

        /**
         * Create a new instance of CustomDialogFragment, providing "num" as an
         * argument.
         */
        fun newInstance(foodJSON : JSONObject, resultCode: Int): SearchResultViewPagerParent {
            val fragmentDialog = SearchResultViewPagerParent()

             //Supply num input as an argument.
            val args = Bundle()
            args.putString("foodOrRecipeJSON", foodJSON.toString())
            args.putInt("resultCode", resultCode)
            fragmentDialog.arguments = args

            return fragmentDialog
        }
    }
}
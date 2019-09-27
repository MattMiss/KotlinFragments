package com.mattmiss.kotlinfragments.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mattmiss.kotlinfragments.adapters.FoodPagerAdapter
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.adapters.RecipePagerAdapter
import kotlinx.android.synthetic.main.pager_tabs.*
import org.json.JSONObject


class SearchResultViewPagerParent : androidx.fragment.app.DialogFragment(){

    var adapter : Any? = null


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

            adapter = FoodPagerAdapter(childFragmentManager)

            // Null Safety Check
            val tempAdapter = adapter as FoodPagerAdapter

            if (tempAdapter != null){
                tempAdapter.setJSONObject(foodOrRecipeJSON)
                pager.adapter = tempAdapter
            }


        }else if (resultCode == 2){
            foodTitleText.text = foodOrRecipeJSON.get("recipe_name").toString()

            println("TESTTTTTT")

            val servingsJSON = JSONObject(foodOrRecipeJSON.get("serving_sizes").toString()).get("serving").toString()

            println(servingsJSON)

            // Null Safety Check
            adapter = RecipePagerAdapter(childFragmentManager)

            val tempAdapter = adapter as RecipePagerAdapter

            if (tempAdapter != null){
                tempAdapter.setJSONObject(foodOrRecipeJSON)
                pager.adapter = tempAdapter
            }
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
            val newFragment = AddNotesBeforeItemSave.newInstance(foodSave)
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
            newFragment.setTargetFragment(this, 2)

            ft.addToBackStack("fragment_foodSave_dialog")
            newFragment.show(ft, "fragment_foodSave_dialog")
        }else if(resultCode == 2){
            val newFragment = AddNotesBeforeItemSave.newInstance(foodSave)
            newFragment.isFood = false
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
            newFragment.setTargetFragment(this, 1)
            ft.addToBackStack("fragment_recipeSave_dialog")
            newFragment.show(ft, "fragment_recipeSave_dialog")
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 2){
                (adapter as FoodPagerAdapter).notifyDataSetChanged()

                val savedItem = JSONObject(data?.getStringExtra("saved_food_item"))

                val name = savedItem.getString("food_name")

                Toast.makeText(activity, name, Toast.LENGTH_SHORT).show()
            }
            else if (requestCode == 1){
                (adapter as RecipePagerAdapter).notifyDataSetChanged()

                val savedItem = JSONObject(data?.getStringExtra("saved_recipe_item"))

                val name = savedItem.getString("recipe_name")

                Toast.makeText(activity, name, Toast.LENGTH_SHORT).show()
            }

            dismiss()
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
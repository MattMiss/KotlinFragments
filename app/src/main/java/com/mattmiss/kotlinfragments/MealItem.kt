package com.mattmiss.kotlinfragments

import org.json.JSONObject

class MealItem {
    var meal = JSONObject()
    var foodItems = ArrayList<FoodItem>()

    fun setFoodItems(){

    }

    companion object{
        fun newInstance(meal : JSONObject) : MealItem{
            val mealItem = MealItem()

            mealItem.meal = meal

            return mealItem
        }
    }
}
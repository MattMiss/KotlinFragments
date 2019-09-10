package com.mattmiss.kotlinfragments

import org.json.JSONArray
import org.json.JSONObject

class MealItem {
    var meal = JSONObject()
    var foodItems = ArrayList<FoodItem>()
    var date = " "

    var containsServDesc = false
    var containsCalories = false
    var containsCarbs = false
    var containsProtein = false
    var containsSodium = false
    var containsFat = false
    var containsSugar = false
    var containsFiber = false

    var totalCaloriesAmount = 0.0
    var totalCarbsAmount = 0.0
    var totalProteinAmount = 0.0
    var totalSodiumAmount = 0.0
    var totalFatAmount = 0.0
    var totalSugarAmount = 0.0
    var totalFiberAmount = 0.0

    fun init(){
        setFoodItems()
        setDate()
    }

    fun setFoodItems(){
        val foodItemsJSON = meal.getJSONArray("all_meals") as JSONArray

        println("------------------${foodItemsJSON}")
        println("------------------${foodItemsJSON.length()}")

        // iterate through each foodItem in the main Meal
        for (i in 0 until foodItemsJSON.length()){

            println(i)

            // turn that food JSON into an actual FoodItem
            val foodItem = FoodItem.newInstance(foodItemsJSON.get(i) as JSONObject)

            println(foodItemsJSON.get(i).toString())

            foodItem.setValues()

            println("-------------SETTING FoodItem: ${foodItem.containsCalories}")

            // add the FoodItem into the Array of FoodItems
            foodItems.add(foodItem)
            addToValues(foodItem)
        }
    }

    fun setDate(){
        date = meal.getString("date")
    }

    fun tempGetChosenMeals() : ArrayList<JSONObject> {
        val chosenMeals = ArrayList<JSONObject>()
        foodItems.forEach { chosenMeals.add(it.foodJSON) }

        return chosenMeals
    }

    fun addToValues(foodItem: FoodItem){
        var servingAmount = 1.0f

        if (foodItem.containsServAmount){
            servingAmount = foodItem.servingAmount.toFloat()
            print("CONTAINS SERVINGGGGG AMOUNT")
        }

        if (foodItem.containsCalories){
            println("Contains ${foodItem.caloriesAmount} Calories")
            totalCaloriesAmount += (foodItem.caloriesAmount.toInt() * servingAmount)
            println("${foodItem.foodName} contains Calories")
        }
        if (foodItem.containsCarbs){
            totalCarbsAmount += (foodItem.carbsAmount.toDouble() * servingAmount)
            println("${foodItem.foodName} contains Carbs")
        }
        if (foodItem.containsProtein){
            totalProteinAmount += (foodItem.proteinAmount.toDouble() * servingAmount)
            println("${foodItem.foodName} contains Protein")
        }
        if (foodItem.containsSodium){
            totalSodiumAmount += (foodItem.sodiumAmount.toDouble() * servingAmount)
            println("${foodItem.foodName} contains Sodium")
        }
        if (foodItem.containsFat){
            totalFatAmount += (foodItem.fatAmount.toDouble() * servingAmount)
            println("${foodItem.foodName} contains Fat")
        }
        if (foodItem.containsSugar){
            totalSugarAmount += (foodItem.sugarAmount.toDouble() * servingAmount)
            println("${foodItem.foodName} contains Sugar")
        }
        if (foodItem.containsFiber){
            totalFiberAmount += (foodItem.fiberAmount.toDouble() * servingAmount)
            println("${foodItem.foodName} contains Fiber")
        }
    }


    companion object{
        fun newInstance(meal : JSONObject) : MealItem{
            val mealItem = MealItem()

            mealItem.meal = meal

            return mealItem
        }
    }
}
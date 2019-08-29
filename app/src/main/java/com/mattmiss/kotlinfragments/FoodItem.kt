package com.mattmiss.kotlinfragments

import com.mattmiss.kotlinfragments.utils.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FoodItem {

    lateinit var foodJSON: JSONObject
    var isFood = false
    var isBrand = false
    var isRecipe = false

    var foodName = ""
    var brandName = ""
    var recipeDescription = ""
    var firstServing = JSONObject()

    var containsServDesc = false
    var containsCalories = false
    var containsCarbs = false
    var containsProtein = false
    var containsSodium = false
    var containsFat = false
    var containsSugar = false

    var servingDescription = ""
    var caloriesAmount = ""
    var carbsAmount = ""
    var proteinAmount = ""
    var sodiumAmount = ""
    var fatAmount = ""
    var sugarAmount = ""

    val warningsList =
        arrayListOf("High Carbs", "High Fat", "High Sugar", "High Sodium")

    var currentWarningList = arrayListOf<String>()

    fun setValues(){
        if (foodJSON != null){
            val namesArray = foodJSON.names().toString()

            // Check if it is food/brand or recipe
            if (namesArray.contains("food", true)){
                isFood = true
                foodName = foodJSON.getString("food_name")

                // check if it is a brand or not
                if  (foodJSON.toString().contains("brand_name")){
                    isBrand = true
                    brandName = foodJSON.getString("brand_name")
                }

                if (namesArray.contains("servings", true)){
                    val tempServings = foodJSON.getJSONObject("servings")
                    var firstServingString = ""

                    val servingNames = tempServings.names() as JSONArray

                    //println(servingNames.length())
                    //println(servingNames.toString())
                    //Utils.longInfo(tempServings.toString())

                    try{
                        firstServingString = tempServings.getJSONArray("serving").get(0).toString()
                    }catch(e : JSONException){
                        firstServingString = tempServings.getJSONObject("serving").toString()
                    }

                    firstServing = JSONObject(firstServingString)

                    if (firstServing.toString().contains("serving_description", true)){
                        containsServDesc = true
                        servingDescription = firstServing.getString("serving_description")
                    }
                    if (firstServing.toString().contains("calories", true)) {
                        containsCalories = true
                        caloriesAmount = firstServing.getString("calories")
                    }
                    if (firstServing.toString().contains("carbohydrate", true)) {
                        containsCarbs = true
                        carbsAmount = firstServing.getString("carbohydrate")
                    }
                    if (firstServing.toString().contains("protein", true)){
                        containsProtein = true
                        proteinAmount = firstServing.getString("protein")
                    }
                    if (firstServing.toString().contains("sodium", true)) {
                        containsSodium = true
                        sodiumAmount = firstServing.getString("sodium")
                    }
                    if (firstServing.toString().contains("fat", true)) {
                        containsFat = true
                        fatAmount = firstServing.getString("fat")
                    }
                    if (firstServing.toString().contains("sugar", true)) {
                        containsSugar = true
                        sugarAmount = firstServing.getString("sugar")
                    }

                    setWarnings(foodJSON)
                }

            // if not a food, check if it's a recipe
            }else if(namesArray.contains("recipe", true)){
                isRecipe = true
                foodName = foodJSON.getString("recipe_name")
                recipeDescription = foodJSON.getString("recipe_description")
            }
        }
    }


    private fun setWarnings(serving : JSONObject){
        // Make sure it contains calories since we need this number for calcs
        if (containsCalories){
            // 4 cals/g, 45-65% limit
            if (containsCarbs){
                println("Carbs: $carbsAmount")
                currentWarningList.add(warningsList[0])
            }
            // 9 cals/g, 20-35% limit
            if (containsFat){
                println("Fat: $fatAmount")
                currentWarningList.add(warningsList[1])
            }
            // 4 cals/g, < 10%
            if (containsSugar){
                println("Sugar: $sugarAmount")
                currentWarningList.add(warningsList[2])
            }
            // 2300g limit
            if (containsSodium){
                println("Sodium: $sodiumAmount")
                currentWarningList.add(warningsList[3])
            }
        }

        //currentWarningList = warningsList
    }

    companion object{
        fun newInstance(foodSave : JSONObject) : FoodItem{
            val foodItem = FoodItem()

            foodItem.foodJSON = foodSave

            return foodItem
        }
    }
}
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
    var containsServAmount = false
    var containsCalories = false
    var containsCarbs = false
    var containsProtein = false
    var containsSodium = false
    var containsFat = false
    var containsSugar = false
    var containsFiber = false

    var servingDescription = ""
    var servingAmount = ""
    var caloriesAmount = ""
    var carbsAmount = ""
    var proteinAmount = ""
    var sodiumAmount = ""
    var fatAmount = ""
    var sugarAmount = ""
    var fiberAmount = ""

    val warningsList =
        arrayListOf("High Carbs", "High Fat", "High Sugar", "High Sodium")

    var currentWarningList = arrayListOf<String>()

    fun setValues(){
        println("Checking Values")
        if (foodJSON != null){
            val namesArray = foodJSON.names().toString()

            if (namesArray.contains("serving_amount", true)){
                containsServAmount = true
                servingAmount = foodJSON.getString("serving_amount")
            }

            // Check if it is food/brand or recipe
            if (namesArray.contains("food", true)){
                isFood = true
                foodName = foodJSON.getString("food_name")
                println(namesArray)
                // check if it is a brand or not
                if  (namesArray.contains("brand_name", true)){
                    isBrand = true
                    brandName = foodJSON.getString("brand_name")
                }

                if (namesArray.contains("serving", true)){
                    println("FoodJSON: ${foodJSON}")
                    var tempServingString = ""

                    try{
                        tempServingString = foodJSON.getJSONObject("servings").toString()
                    }catch(e : JSONException){
                        tempServingString = foodJSON.getJSONObject("serving").toString()
                    }

                    val tempServings = JSONObject(tempServingString)

                    println("TempServingJSON: ${tempServings}")

                    var firstServingString = ""

                    try{
                        firstServingString = tempServings.getJSONArray("serving").get(0).toString()
                    }catch(e : JSONException){
                        firstServingString = tempServings.getJSONObject("serving").toString()
                    }

                    firstServing = JSONObject(firstServingString)

                    println("FoodJSON First Serving: ${firstServing}")
3
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
                    if (firstServing.toString().contains("fiber", true)) {
                        containsFiber = true
                        fiberAmount = firstServing.getString("fiber")
                    }

                    setWarnings(foodJSON)
                }

            // if not a food, check if it's a recipe
            }else if(namesArray.contains("recipe", true)){
                isRecipe = true
                foodName = foodJSON.getString("recipe_name")
                recipeDescription = foodJSON.getString("recipe_description")


                if (foodJSON.names().toString().contains("serving",true)){

                    try{
                        val serving = foodJSON.getJSONObject("serving_sizes")
                            .getJSONObject("serving")

                        if (serving.toString().contains("calories", true)) {
                            containsCalories = true
                            caloriesAmount = serving.get("calories").toString()
                        }
                        if (serving.toString().contains("carbohydrate", true)) {
                            containsCarbs = true
                            carbsAmount = serving.getString("carbohydrate")
                        }
                        if (serving.toString().contains("protein", true)){
                            containsProtein = true
                            proteinAmount = serving.getString("protein")
                        }
                        if (serving.toString().contains("sodium", true)) {
                            containsSodium = true
                            sodiumAmount = serving.getString("sodium")
                        }
                        if (serving.toString().contains("fat", true)) {
                            containsFat = true
                            fatAmount = serving.getString("fat")
                        }
                        if (serving.toString().contains("sugar", true)) {
                            containsSugar = true
                            sugarAmount = serving.getString("sugar")
                        }
                        if (serving.toString().contains("fiber", true)) {
                            containsFiber = true
                            fiberAmount = serving.getString("fiber")
                        }

                    }catch (e : JSONException){

                    }


                }
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
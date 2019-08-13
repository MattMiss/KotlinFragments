package com.mattmiss.kotlinfragments.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import org.json.JSONObject
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.inflate
import kotlinx.android.synthetic.main.search_result_item.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattmiss.kotlinfragments.utils.Utils
import org.json.JSONArray
import org.json.JSONException


class SearchResultsAdapter(private val foodItem : ArrayList<JSONObject>, val clickListener: (JSONObject, Int) -> Unit): RecyclerView.Adapter<SearchResultsAdapter.FoodHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val inflatedView = parent.inflate(R.layout.search_result_item, false)
        return FoodHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return foodItem.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val foodSave = foodItem[position]

        holder.bindFood(foodSave, clickListener)

    }

    //1
    class FoodHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var foodSave: JSONObject? = null


        //3
        init {
            //view.setOnClickListener(this)

        }

        //4
        override fun onClick(v: View) {
           // Log.d("RecyclerView", "CLICK!")

        }

        fun bindFood(foodSave: JSONObject, clickListener: (JSONObject, Int) -> Unit) {
            this.foodSave = foodSave
            view.expandableLayout_result.collapse()

            val tempList =
                arrayListOf("Apple", "Mango", "Strawberry", "Pineapple", "Orange", "Blueberry", "Watermelon")

            val adapter = NutrientAlertAdapter(tempList)
            val layoutManager = LinearLayoutManager(view.context)
            val layout2 = GridLayoutManager(view.context, 3)
            view.recyclerView.adapter = adapter
            view.recyclerView.layoutManager = layout2


            val namesArray = foodSave.names().toString()

            if (namesArray.contains("food", true)){
                    view.labelName.text = foodSave.getString("food_name")
                    view.labelDescription.text = foodSave.getString("food_type")

                    if (namesArray.contains("servings", true)){
                        val tempServings = foodSave.getJSONObject("servings")
                        var firstServingString = ""

                        val servingNames = tempServings.names() as JSONArray

                        println(servingNames.length())
                        println(servingNames.toString())
                        Utils.longInfo(tempServings.toString())

                        try{
                            firstServingString = tempServings.getJSONArray("serving").get(0).toString()
                        }catch(e : JSONException){
                            firstServingString = tempServings.getJSONObject("serving").toString()
                        }

                        val firstServing = JSONObject(firstServingString)

                        if (firstServing.toString().contains("serving_description", true)){
                            view.servingDescription.text = firstServing.getString("serving_description")
                        }
                        if (firstServing.toString().contains("calories", true)) {
                            view.caloriesNumber.text = firstServing.getString("calories")
                        }
                        if (firstServing.toString().contains("carbohydrate", true)) {
                            view.carbsNumber.text = firstServing.getString("carbohydrate")
                        }
                        if (firstServing.toString().contains("protein", true)){
                            view.proteinNumber.text = firstServing.getString("protein")
                        }
                        if (firstServing.toString().contains("sodium", true)) {
                            view.sodiumNumber.text = firstServing.getString("sodium")
                        }
                        if (firstServing.toString().contains("fat", true)) {
                            view.fatNumber.text = firstServing.getString("fat")
                        }
                    }

                if  (foodSave.toString().contains("brand")){
                    view.icon.setImageResource(R.drawable.ic_brandicon)
                }
                    // Set the click listener to the listener provided in the constructor
                    view.setOnClickListener { clickListener(foodSave,
                        FOOD_ID
                    ) }

            }else if(namesArray.contains("recipe", true)){
                    view.labelName.text = foodSave.getString("recipe_name")
                    view.labelDescription.text = foodSave.getString("recipe_description")

                    view.icon.setImageResource(R.drawable.ic_recipeicon)

                    // Set the click listener to the listener provided in the constructor
                    view.setOnClickListener { clickListener(foodSave,
                        RECIPE_ID
                    ) }
            }

            view.expandableButton_result.setOnClickListener{
                view.expandableLayout_result.toggle()
            }
        }

        companion object{
            val FOOD_ID = 1
            val RECIPE_ID = 2
        }

    }
}
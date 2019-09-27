package com.mattmiss.kotlinfragments.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import org.json.JSONObject
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.inflate
import kotlinx.android.synthetic.main.search_result_item.view.*
import com.mattmiss.kotlinfragments.FoodItem


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
        private var foodItem: FoodItem? = null


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

            // Set the FoodItem from the foodSave JSONObject
            foodItem = FoodItem.newInstance(foodSave)
            foodItem?.setValues()

            view.expandableLayout_result.collapse()

            // Get the list of warnings that the foodItem calculated in it's class
            val warningAdapter = NutrientAlertAdapter(foodItem!!.currentWarningList)

            // Only used if I want a linear layout for warnings
            //val layoutManager = LinearLayoutManager(view.context)

            // layout shows 3 warnings per row
            val gridLayout = GridLayoutManager(view.context, 4)
            view.warningRecycler.adapter = warningAdapter
            view.warningRecycler.layoutManager = gridLayout


            if (foodItem!!.isFood){
                view.labelName.text = foodItem!!.foodName
                view.labelDescription.visibility = View.GONE

                if (foodItem!!.containsServDesc){
                    view.servingDescription.setText(foodItem!!.servingDescription)
                }
                if (foodItem!!.containsCalories){
                    view.caloriesNumber.text = foodItem!!.caloriesAmount
                }
                if (foodItem!!.containsCarbs){
                    view.carbsNumber.text = foodItem!!.carbsAmount
                }
                if (foodItem!!.containsProtein){
                    view.proteinNumber.text = foodItem!!.proteinAmount
                }
                if (foodItem!!.containsSodium){
                    view.sodiumNumber.text = foodItem!!.sodiumAmount
                }
                if (foodItem!!.containsFat){
                    view.fatNumber.text = foodItem!!.fatAmount
                }

                if (foodItem!!.isBrand){
                    view.icon.setImageResource(R.drawable.ic_brandicon)
                    view.labelDescription.text = foodItem!!.brandName
                    view.labelDescription.visibility = View.VISIBLE
                }

                // Set the click listener to the listener provided in the constructor
                view.setOnClickListener { clickListener(foodSave,
                    FOOD_ID
                ) }
                // servingDescription needs its own onClick for some reason
                view.servingDescription.setOnClickListener{ clickListener(foodSave,
                    FOOD_ID
                ) }

            }else if(foodItem!!.isRecipe){
                view.labelName.text = foodItem!!.foodName
                view.labelDescription.text = foodItem!!.recipeDescription

                view.icon.setImageResource(R.drawable.ic_recipeicon)


                // Set the click listener to the listener provided in the constructor
                view.setOnClickListener { clickListener(foodSave,
                    RECIPE_ID
                ) }
                // servingDescription needs its own onClick for some reason
                view.servingDescription.setOnClickListener{ clickListener(foodSave,
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
package com.mattmiss.kotlinfragments.fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.adapters.ChosenMealsListAdapter
import com.mattmiss.kotlinfragments.adapters.SavedItemListAdapter
import com.mattmiss.kotlinfragments.utils.Utils
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*







class AddMealDialogFragment : androidx.fragment.app.DialogFragment(), ChooseMealDialogFragment.MealChoiceDialogListener{

    private var chosenMeals = arrayListOf<JSONObject>()
    private var currentChoice = JSONObject()
    private var servingChoices = arrayListOf<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var adapter: ChosenMealsListAdapter
    private var dateChosen = false

    private var listener: AddMealToDayListener? = null

    interface AddMealToDayListener {
        fun onDone(dayMeals: JSONObject)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_meal_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChosenMealsListAdapter(
            context!!,
            { savedItem: JSONObject -> mealClicked(savedItem) })

        recyclerMealsChosen.adapter = adapter

        recyclerMealsChosen.layoutManager = LinearLayoutManager(recyclerMealsChosen.context)

        // Add a line in between each item
        recyclerMealsChosen.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerMealsChosen.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        dateLayout.setOnClickListener{
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(activity,
                DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay -> setDate(mMonth, mDay, mYear)
            }, year, month, day)

            //isDateChosen = true
            datePickerDialog.show()
        }

        mealLayout.setOnClickListener{
            val ft = activity!!.supportFragmentManager.beginTransaction()

            val newFragment = ChooseMealDialogFragment.newInstance()
            // Listener in ChooseMeal wont work without setting it
            newFragment.setListener(this)
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("fragment_add_meal_dialog")
            newFragment.show(ft, "fragment_add_meal_dialog")
        }



        btnDone.setOnClickListener{
            var allMeals = JSONObject()
            allMeals.put("date", date.text)

            var mealsArray = JSONArray()

            chosenMeals.forEach { mealsArray.put(it) }

            allMeals.put("all_meals", mealsArray)

            // Call the onDone function within FragmentMeals (which adds the meal to the database)
            listener?.onDone(allMeals)
            dismiss()
        }


        arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, servingChoices)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerServingSize.adapter = arrayAdapter
        spinnerServingSize.prompt = "Serving Size"


        // Set Whole Number Picker
        servingAmountWholeNumber.minValue = 0
        servingAmountWholeNumber.maxValue = 99

        val wholeNumVals = Array(100) { it.toString() }
        wholeNumVals[0] = " "
        servingAmountWholeNumber.displayedValues = wholeNumVals


        // Set Fraction Number Picker
        servingAmountFractionNumber.minValue = 0
        servingAmountFractionNumber.maxValue = 7

        val fractionNumVals = arrayOf(" ", "1/8", "1/4", "3/8", "1/2", "5/8", "3/4", "7/8")
        servingAmountFractionNumber.displayedValues = fractionNumVals


        btnAddChoice.setOnClickListener{
            var mealReady = false

            if (dateChosen){
                if (mealText.text.equals("Choose Meal")){
                    Toast.makeText(activity, "Please chose a meal", Toast.LENGTH_SHORT).show()
                }else{
                    // Check if either serving size Number pickers are empty before adding the meal
                    if (servingAmountWholeNumber.value != 0 || servingAmountFractionNumber.value != 0){
                        mealReady = true
                    }else{
                        Toast.makeText(activity, "Serving size can't be empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(activity, "Please choose a date", Toast.LENGTH_SHORT).show()
            }
            // If date is chosen
            if (mealReady){
                addCurrentChoice(currentChoice)
                println(currentChoice.toString())
            }

        }
    }

    override fun onChoice(meal: JSONObject) {
        setCurrentChoice(meal)
    }

    companion object{
        fun newInstance() : AddMealDialogFragment{
            val addMealDialog = AddMealDialogFragment()

            return addMealDialog
        }
    }

    fun setListener(listener: AddMealToDayListener) {
        this.listener = listener
    }

    fun setDate(month: Int, day: Int, year: Int){
        date.setText("${month + 1}-$day-$year")
        dateChosen = true
    }

    fun setCurrentChoice(meal: JSONObject){
        currentChoice = meal

        if (meal.toString().contains("recipe_name")){
            mealText.text = meal.get("recipe_name").toString()
            setServingChoiceArray(meal, 1)
        }else{
            mealText.text = meal.get("food_name").toString()
            setServingChoiceArray(meal, 2)
        }
    }

    // When the Add button is clicked, this function is called as long as there is a meal chosen and
    // there is a serving amount chosen.
    // This is where I can turn the foodItem object into a shortened Meal object with essential info only
    fun addCurrentChoice(meal:JSONObject){
        var tempMeal = JSONObject(meal.toString())

        val servingAmount = servingAmountWholeNumber.value + (servingAmountFractionNumber.value * .125)
        val servingType = spinnerServingSize.selectedItem

        tempMeal.put("serving_amount", servingAmount)
        tempMeal.put("serving_type", servingType)

        chosenMeals.add(tempMeal)
        adapter.setSavedItems(chosenMeals)
        chosenMeals.forEach { Utils.longInfo(it.toString()) }
        //setCurrentChoice(meal)
    }


    // type 1 is recipe, type 2 is food
    fun setServingChoiceArray(meal: JSONObject, type : Int){
        servingChoices.clear()

        if (type == 1){
            val tempServings = meal.get("serving_sizes") as JSONObject
            val serving = tempServings.get("serving") as JSONObject

            // Change this later, serving size looks weird
            servingChoices.add(serving.get("serving_size").toString())
        }else{
            val tempServings = (meal.get("servings") as JSONObject).get("serving")

            println(tempServings.toString())

            if (tempServings is JSONObject){
                servingChoices.add(tempServings.get("measurement_description").toString())

            }else if (tempServings is JSONArray){
                var x = 0
                while(x < tempServings.length()){
                    val serving = tempServings.get(x) as JSONObject
                    println("SSEEERRRRVINGGGGGGGGGGGGGGGGGGGGGGGG $x - ${serving.get("measurement_description")}")
                    servingChoices.add(serving.get("measurement_description").toString())
                    x++
                }
            }
        }

        arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, servingChoices)
        spinnerServingSize.adapter = arrayAdapter
        spinnerServingSize.setSelection(0, true)
    }


    private fun mealClicked(savedItem : JSONObject) {
        val ft = activity!!.supportFragmentManager.beginTransaction()

        if (savedItem.toString().contains("recipe")){
            val newFragment = SavedRecipeDialogFragment.newInstance(savedItem)
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("recipe_fragment_dialog")
            newFragment.show(ft, "recipe_fragment_dialog")
        }else{
            val newFragment = SavedFoodViewPagerParent.newInstance(savedItem)
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("fragment_dialog")
            newFragment.show(ft, "fragment_dialog")
        }



    }

}


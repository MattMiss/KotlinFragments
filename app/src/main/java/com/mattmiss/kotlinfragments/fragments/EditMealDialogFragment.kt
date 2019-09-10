package com.mattmiss.kotlinfragments.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.adapters.ChosenMealsListAdapter
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import org.json.JSONObject
import java.util.*
import lecho.lib.hellocharts.model.SliceValue
import android.graphics.Color
import com.mattmiss.kotlinfragments.MealItem
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.date
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.dateLayout
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.recyclerMealsChosen
import kotlinx.android.synthetic.main.fragment_edit_meal_dialog.*
import lecho.lib.hellocharts.model.PieChartData
import kotlin.collections.ArrayList


class EditMealDialogFragment : androidx.fragment.app.DialogFragment(){

    private var chosenObject = JSONObject()
    private lateinit var mealItem : MealItem

    private var chosenMeals = arrayListOf<JSONObject>()

    private lateinit var adapter: ChosenMealsListAdapter

    private var dateChosen = false

    private var listener: AddMealToDayListener? = null
    private lateinit var savedItemViewModel: SavedItemViewModel


    // using about averages from Atwater conversions (https://www.nutribase.com/atwater.html)
    private val CARBS_TO_CALS = 3.9f
    private val PROTEIN_TO_CALS = 3.5f
    private val FAT_TO_CALS = 8.37f
    private val SUGAR_TO_CALS = 4f
    private val FIBER_TO_CALS = 2f


    interface AddMealToDayListener {
        fun onDone(dayMeals: JSONObject)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_meal_dialog, container, false)
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

        // sets the chosenObject and the mealItem
        setChosenObject()

        // sets the date
        setDate(mealItem.date)

        // a temp way of getting a chosenMeals list
        chosenMeals = mealItem.tempGetChosenMeals()

        // set the adapter using the meals(MealItem.FoodItems) in the MealItem
        adapter.setSavedItems(chosenMeals)

        // Sets all the numbers and pie chart percentages
        setOverallNumbers()


        btnBack.setOnClickListener{
            dismiss()
        }

    }

    companion object{

        fun newInstance(savedItem: JSONObject) = EditMealDialogFragment().apply {
            arguments = Bundle().apply {
                putString("chosenItem", savedItem.toString())
            }
        }
    }

    fun setChosenObject(){
        chosenObject = JSONObject(arguments?.getString("chosenItem"))
        mealItem = MealItem.newInstance(chosenObject)
        mealItem.init()
    }


    // used to set date from loaded MealItem
    fun setDate(newDate : String){
        date.setText(newDate)
        dateChosen = true
    }

    // used when datePicker is used
    fun setDate(month: Int, day: Int, year: Int){
        date.setText("${month + 1}-$day-$year")
        dateChosen = true
    }



    fun setOverallNumbers(){
        val calories = mealItem.totalCaloriesAmount.toString()
        caloriesAmount.text = calories

        val carbs = ("%.2f".format(mealItem.totalCarbsAmount))
        carbsAmount.text = carbs

        val protein = ("%.2f".format(mealItem.totalProteinAmount))
        proteinAmount.text = protein

        //val sodium = ("%.2f".format(mealItem.totalSodiumAmount))
        //sodiumAmount.text = sodium

        val fat = ("%.2f".format(mealItem.totalFatAmount))
        fatAmount.text = fat

        val sugar = ("%.2f".format(mealItem.totalSugarAmount))
        sugarAmount.text = sugar

        val fiber = ("%.2f".format(mealItem.totalFiberAmount))
        fiberAmount.text = fiber



        // set the pie labels and slice amounts
        setPieLabels(calories.toFloat(), carbs.toFloat(), protein.toFloat(), fat.toFloat()
            , sugar.toFloat(), fiber.toFloat())


        //val barParams = carbsActualBar.layoutParams
        //Toast.makeText(activity, "${barParams.width}", Toast.LENGTH_SHORT).show()
        //val multiplier = .2
        //barParams.width = (barParams.width * multiplier).roundToInt()


    }


    fun setPieLabels(calsIn : Float, carbsIn : Float, proteinIn : Float, fatIn : Float,
                     sugarIn : Float, fiberIn : Float){
        val pieData = ArrayList<SliceValue>()


        val totalCalories = calsIn

        val totalCarbs = carbsIn * CARBS_TO_CALS
        val totalProtein = proteinIn * PROTEIN_TO_CALS
        val totalFat = fatIn * FAT_TO_CALS
        val totalSugar = sugarIn * SUGAR_TO_CALS
        val totalFiber = fiberIn * FIBER_TO_CALS

        // This total temp calories might be slightly different than the calsIn number
        // We will calculate new percentages based off these numbers, then convert to the
        // calsIn number using the new percentages
        val totalTempCalories = totalCarbs + totalProtein + totalFat + totalSugar + totalFiber


        val carbPercent = totalCarbs / totalTempCalories
        val proteinPercent = totalProtein / totalTempCalories
        val fatPercent = totalFat / totalTempCalories
        val sugarPercent = totalSugar / totalTempCalories
        val fiberPercent = totalFiber / totalTempCalories

        guidelineCarbsActualAmount.setGuidelinePercent(carbPercent)
        carbsNum.text = carbsIn.toString()

        guidelineProteinActualAmount.setGuidelinePercent(proteinPercent)
        proteinNum.text = proteinIn.toString()

        guidelineFatActualAmount.setGuidelinePercent(fatPercent)
        fatNum.text = fatIn.toString()

        guidelineSugarActualAmount.setGuidelinePercent(sugarPercent)
        sugarNum.text = sugarIn.toString()

        guidelineFiberActualAmount.setGuidelinePercent(fiberPercent)
        fiberNum.text = fiberIn.toString()

        val carbAmountNew = carbPercent * calsIn
        val proteinbAmountNew = proteinPercent * calsIn
        val fatAmountNew = fatPercent * calsIn
        val sugarAmountNew = sugarPercent * calsIn
        val fiberAmountNew = fiberPercent * calsIn


        // yellow carbs
        pieData.add(SliceValue(carbPercent, Color.parseColor("#ffca42")).setLabel("${"%.2f".format(carbPercent * 100)}%"))
        // red protein
        pieData.add(SliceValue(proteinPercent, Color.parseColor("#ff4040")).setLabel("${"%.2f".format(proteinPercent * 100)}%"))
        // purple fat
        pieData.add(SliceValue(fatPercent, Color.parseColor("#b573eb")).setLabel("${"%.2f".format(fatPercent * 100)}%"))
        // blue sugar
        pieData.add(SliceValue(sugarPercent, Color.parseColor("#4db5ff")).setLabel("${"%.2f".format(sugarPercent * 100)}%"))
        // green fiber
        pieData.add(SliceValue(fiberPercent, Color.parseColor("#39ad47")).setLabel("${"%.2f".format(fiberPercent * 100)}%"))

        val pieChartData = PieChartData(pieData)
        pieChartData.setHasCenterCircle(true).setCenterCircleScale(.3f).setCenterCircleColor(Color.parseColor("#00FFFFFF"))
        // set the space between each slice to the minimum (other than 0)
        pieChartData.slicesSpacing = 1
        pieChartData.setHasLabels(true)
        pieChartData.isValueLabelBackgroundAuto = false
        pieChartData.valueLabelBackgroundColor = Color.parseColor("#00FFFFFF")

        pieChartNutrients.pieChartData = pieChartData
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


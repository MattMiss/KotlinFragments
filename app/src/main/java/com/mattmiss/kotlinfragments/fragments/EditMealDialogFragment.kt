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
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import lecho.lib.hellocharts.model.SliceValue
import android.graphics.Color
import com.mattmiss.kotlinfragments.utils.Utils
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.btnAddChoice
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.date
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.dateLayout
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.recyclerMealsChosen
import kotlinx.android.synthetic.main.fragment_edit_meal_dialog.*
import lecho.lib.hellocharts.model.PieChartData
import kotlin.collections.ArrayList


class EditMealDialogFragment : androidx.fragment.app.DialogFragment(){

    private var chosenObject = JSONObject()

    private var chosenMeals = arrayListOf<JSONObject>()


    private lateinit var adapter: ChosenMealsListAdapter

    private var dateChosen = false

    private var listener: AddMealToDayListener? = null
    private lateinit var savedItemViewModel: SavedItemViewModel

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



        btnAddChoice.setOnClickListener{


        }

        getAndSetDate(chosenObject)
        getAndSetMeals(chosenObject)

        setOverallNumbers(chosenMeals)

        setLabels(chosenMeals)

    }

    companion object{
        fun newInstance(savedItem: JSONObject) : EditMealDialogFragment{
            val addMealDialog = EditMealDialogFragment()

            addMealDialog.chosenObject = savedItem

            return addMealDialog
        }
    }

    fun setDate(month: Int, day: Int, year: Int){
        date.setText("${month + 1}-$day-$year")
        dateChosen = true
    }

    fun getAndSetDate(savedItem: JSONObject){
        val chosenDate = savedItem.get("date")

        date.setText("$chosenDate")
        dateChosen = true
    }

    fun getAndSetMeals(savedItem: JSONObject){
        val meals = savedItem.get("all_meals") as JSONArray

        var x = 0

        while (x < meals.length()){
            chosenMeals.add(meals.get(x) as JSONObject)
            x++
        }
        adapter.setSavedItems(chosenMeals)
    }


    fun setOverallNumbers(meals: ArrayList<JSONObject>){

    }


    fun setLabels(meals: ArrayList<JSONObject>){
        meals.forEach { Utils.longInfo(it.toString()) }


        setPieLabels()
    }

    fun setPieLabels(){
        val pieData = ArrayList<SliceValue>()

        pieData.add(SliceValue(15f, Color.BLUE))
        pieData.add(SliceValue(25f, Color.GRAY))
        pieData.add(SliceValue(10f, Color.RED))
        pieData.add(SliceValue(60f, Color.MAGENTA))

        val pieChartData = PieChartData(pieData)
        pieChartData.setHasCenterCircle(true).setCenterCircleScale(.45f).setCenterCircleColor(Color.parseColor("#EEFFFFFF"))


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


package com.mattmiss.kotlinfragments.fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.adapters.SavedItemListAdapter
import kotlinx.android.synthetic.main.fragment_add_meal_dialog.*
import org.json.JSONObject
import java.util.*







class AddMealDialogFragment : androidx.fragment.app.DialogFragment(), ChooseMealDialogFragment.MealChoiceDialogListener{

    private var chosenMeals = arrayListOf<JSONObject>()
    private var currentChoice = JSONObject()
    private lateinit var adapter: SavedItemListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_meal_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SavedItemListAdapter(
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

        btnChooseDate.setOnClickListener{
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener{ view,
                mYear, mMonth, mDay -> setDate(mMonth, mDay, mYear)
            }, year, month, day)

            //isDateChosen = true
            datePickerDialog.show()
        }

        btnChooseMeal.setOnClickListener{
            val ft = activity!!.supportFragmentManager.beginTransaction()

            val newFragment = ChooseMealDialogFragment.newInstance()
            // Listener in ChooseMeal wont work without setting it
            newFragment.setListener(this)
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("fragment_add_meal_dialog")
            newFragment.show(ft, "fragment_add_meal_dialog")
        }

        btnDone.setOnClickListener{
            dismiss()
        }

        btnAddChoice.setOnClickListener{
            addCurrentChoice(currentChoice)
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

    fun setDate(month: Int, day: Int, year: Int){
        date.setText("${month + 1}-$day-$year")

    }

    fun setCurrentChoice(meal: JSONObject){
        currentChoice = meal
        if (meal.toString().contains("recipe_name")){
            mealText.text = meal.get("recipe_name").toString()
        }else{
            mealText.text = meal.get("food_name").toString()
        }
    }

    fun addCurrentChoice(meal:JSONObject){
        chosenMeals.add(meal)
        adapter.setSavedItems(chosenMeals)
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


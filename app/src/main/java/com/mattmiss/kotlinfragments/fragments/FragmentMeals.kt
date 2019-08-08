package com.mattmiss.kotlinfragments.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattmiss.kotlinfragments.*
import com.mattmiss.kotlinfragments.adapters.SavedItemListAdapter
import com.mattmiss.kotlinfragments.database.Meal
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import kotlinx.android.synthetic.main.fragment_meals.*
import org.json.JSONObject
import java.util.*


class FragmentMeals : androidx.fragment.app.Fragment() {

    private lateinit var savedItemViewModel: SavedItemViewModel

    private var savedMeals = arrayListOf<JSONObject>()
    private var savedDates = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SavedItemListAdapter(
            context!!,
            { savedMeal: JSONObject -> mealClicked(savedMeal) })

        recyclerview.adapter = adapter

        recyclerview.layoutManager = LinearLayoutManager(recyclerview.context)

        // Add a line in between each item
        recyclerview.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerview.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        // viewLifecycleOwner is used instead of (this) so it doesnt reattach an observer every time the fragment attaches again
        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)

        savedItemViewModel.allMeals.observe(viewLifecycleOwner, Observer { meals ->
            meals.let { updateMealList(it, adapter) }
        })

        btnAddMeal.setOnClickListener{

            val ft = activity!!.supportFragmentManager.beginTransaction()

            val newFragment = AddMealDialogFragment.newInstance()
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("fragment_add_meal_dialog")
            newFragment.show(ft, "fragment_add_meal_dialog")

        }

    }

    companion object {
        fun newInstance(): FragmentMeals {
            val fragment = FragmentMeals()
            return fragment
        }
    }


    private fun mealClicked(savedItem : JSONObject) {
        /*
        val ft = activity!!.supportFragmentManager.beginTransaction()

        val newFragment = SavedRecipeDialogFragment.newInstance(savedItem)
        newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

        ft.addToBackStack("recipe_fragment_dialog")
        newFragment.show(ft, "recipe_fragment_dialog")
        */

        Toast.makeText(activity, "Meal Clicked!", Toast.LENGTH_SHORT).show()

    }

    fun updateMealList(meals: List<Meal>, adapter : SavedItemListAdapter){

        for (item in meals){
            val tempJSON = JSONObject(item.meal)
            savedMeals.add(tempJSON)
            savedDates.add(tempJSON.get("date").toString())
        }

        adapter.setSavedItems(savedMeals)
    }

}